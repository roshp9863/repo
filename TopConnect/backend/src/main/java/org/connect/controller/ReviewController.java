package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ApiResponse;
import org.connect.dto.ReviewDTO;
import org.connect.security.SecurityUtils;
import org.connect.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reviews", description = "Review & rating endpoints")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Submit a review for a completed booking")
    public ResponseEntity<ApiResponse<ReviewDTO>> submitReview(@Valid @RequestBody ReviewDTO dto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("User {} submitting review for booking {}", userId, dto.getBookingId());
        ReviewDTO review = reviewService.submitReview(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Review submitted successfully", review));
    }

    @GetMapping("/artist/{artistId}")
    @Operation(summary = "Get all reviews for an artist")
    public ResponseEntity<ApiResponse<Page<ReviewDTO>>> getArtistReviews(
            @PathVariable UUID artistId,
            Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getArtistReviews(artistId, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Reviews retrieved", reviews));
    }

    @GetMapping("/artist/{artistId}/summary")
    @Operation(summary = "Get review summary and stats for an artist")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReviewSummary(@PathVariable UUID artistId) {
        Map<String, Object> summary = reviewService.getArtistReviewSummary(artistId);
        return ResponseEntity.ok(new ApiResponse<>("Review summary", summary));
    }

    @GetMapping("/check/{bookingId}")
    @Operation(summary = "Check if current user has reviewed a booking")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkReviewed(@PathVariable UUID bookingId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        boolean reviewed = reviewService.hasReviewed(bookingId, userId);
        return ResponseEntity.ok(new ApiResponse<>("Review check", Map.of("reviewed", reviewed)));
    }
}

