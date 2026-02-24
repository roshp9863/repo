package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ReviewDTO;
import org.connect.entity.ArtistProfile;
import org.connect.entity.Booking;
import org.connect.entity.Review;
import org.connect.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final NotificationService notificationService;

    /**
     * Submit a review for a completed booking.
     * Fan reviews artist OR artist reviews fan — both allowed.
     */
    public ReviewDTO submitReview(UUID reviewerUserId, ReviewDTO dto) {
        if (dto.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID is required");
        }
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("You can only review completed bookings");
        }

        // Check if already reviewed by this user
        if (reviewRepository.existsByBookingIdAndUserId(dto.getBookingId(), reviewerUserId)) {
            throw new IllegalArgumentException("You have already reviewed this booking");
        }

        // Verify reviewer is part of this booking (fan or artist)
        boolean isFan = booking.getUserId().equals(reviewerUserId);
        boolean isArtist = false;
        UUID artistProfileId = booking.getArtistId();
        if (!isFan) {
            var artistProfile = artistProfileRepository.findById(artistProfileId);
            isArtist = artistProfile.isPresent() && artistProfile.get().getUserId().equals(reviewerUserId);
        }
        if (!isFan && !isArtist) {
            throw new IllegalArgumentException("You are not part of this booking");
        }

        // Build review
        String tagsStr = dto.getTags() != null ? String.join(",", dto.getTags()) : null;

        Review review = Review.builder()
                .bookingId(dto.getBookingId())
                .userId(reviewerUserId)
                .artistId(artistProfileId)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .tags(tagsStr)
                .build();

        review = reviewRepository.save(review);
        log.info("Review submitted: id={}, booking={}, rating={}, by={}", review.getId(), dto.getBookingId(), dto.getRating(), reviewerUserId);

        // Recalculate artist rating aggregate (only for fan reviews of artists)
        if (isFan) {
            recalculateArtistRating(artistProfileId);

            // Notify artist
            ArtistProfile artist = artistProfileRepository.findById(artistProfileId).orElse(null);
            if (artist != null) {
                String fanEmail = userAccountRepository.findById(reviewerUserId)
                        .map(u -> u.getEmail()).orElse("A fan");
                notificationService.send(artist.getUserId(), "REVIEW",
                        "New " + dto.getRating() + "★ Review",
                        fanEmail + " left a review: " + (dto.getComment() != null ? dto.getComment() : "No comment"));
            }
        }

        return toReviewDTO(review);
    }

    /**
     * Get all reviews for an artist, sorted by newest first.
     */
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getArtistReviews(UUID artistId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByArtistIdOrderByCreatedAtDesc(artistId, pageable);
        return new PageImpl<>(
                reviews.getContent().stream().map(this::toReviewDTO).collect(Collectors.toList()),
                pageable,
                reviews.getTotalElements()
        );
    }

    /**
     * Get review summary stats for an artist.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getArtistReviewSummary(UUID artistId) {
        List<Review> reviews = reviewRepository.findByArtistId(artistId);
        Map<String, Object> summary = new LinkedHashMap<>();

        if (reviews.isEmpty()) {
            summary.put("averageRating", 0.0);
            summary.put("totalReviews", 0);
            summary.put("ratingDistribution", Map.of(1, 0, 2, 0, 3, 0, 4, 0, 5, 0));
            summary.put("topTags", List.of());
            return summary;
        }

        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0);
        summary.put("averageRating", Math.round(avg * 10.0) / 10.0);
        summary.put("totalReviews", reviews.size());

        // Rating distribution
        Map<Integer, Long> dist = reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));
        Map<Integer, Long> fullDist = new LinkedHashMap<>();
        for (int i = 5; i >= 1; i--) fullDist.put(i, dist.getOrDefault(i, 0L));
        summary.put("ratingDistribution", fullDist);

        // Top tags
        Map<String, Long> tagCounts = reviews.stream()
                .filter(r -> r.getTags() != null && !r.getTags().isBlank())
                .flatMap(r -> Arrays.stream(r.getTags().split(",")))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        List<Map<String, Object>> topTags = tagCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> {
                    Map<String, Object> tag = new LinkedHashMap<>();
                    tag.put("tag", e.getKey());
                    tag.put("count", e.getValue());
                    return tag;
                }).collect(Collectors.toList());
        summary.put("topTags", topTags);

        return summary;
    }

    /**
     * Check if a user has already reviewed a booking.
     */
    @Transactional(readOnly = true)
    public boolean hasReviewed(UUID bookingId, UUID userId) {
        return reviewRepository.existsByBookingIdAndUserId(bookingId, userId);
    }

    /**
     * Recalculate artist's rating aggregate after a new review.
     */
    private void recalculateArtistRating(UUID artistProfileId) {
        Double avgRating = reviewRepository.findAverageRatingByArtistId(artistProfileId);
        long reviewCount = reviewRepository.countByArtistId(artistProfileId);

        ArtistProfile artist = artistProfileRepository.findById(artistProfileId).orElse(null);
        if (artist != null) {
            artist.setRatingAggregate(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP));
            artist.setRatingCount((int) reviewCount);
            artistProfileRepository.save(artist);
            log.info("Recalculated rating for artist={}: avg={}, count={}", artistProfileId, avgRating, reviewCount);
        }
    }

    private ReviewDTO toReviewDTO(Review review) {
        ReviewDTO.ReviewDTOBuilder builder = ReviewDTO.builder()
                .id(review.getId())
                .bookingId(review.getBookingId())
                .userId(review.getUserId())
                .artistId(review.getArtistId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt());

        // Parse tags
        if (review.getTags() != null && !review.getTags().isBlank()) {
            builder.tags(Arrays.asList(review.getTags().split(",")));
        } else {
            builder.tags(List.of());
        }

        // Enrich with names
        userAccountRepository.findById(review.getUserId()).ifPresent(u -> {
            builder.reviewerEmail(u.getEmail());
            // Mask email for privacy: show first 3 chars + ***
            String email = u.getEmail();
            String masked = email.length() > 3 ? email.substring(0, 3) + "***" : email;
            builder.reviewerName(masked);
        });

        artistProfileRepository.findById(review.getArtistId()).ifPresent(a -> {
            builder.artistName(a.getDisplayName());
        });

        return builder.build();
    }
}

