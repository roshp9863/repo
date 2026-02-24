package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.entity.*;
import org.connect.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminService {

    private final UserAccountRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final OfferingRepository offeringRepository;
    private final CommissionConfigRepository commissionConfigRepository;
    private final ArtistCategoryRepository categoryRepository;
    private final NotificationService notificationService;

    // ─────────────────────────────── DASHBOARD ───────────────────────────────

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        long totalUsers = userRepository.count();
        long totalArtists = artistProfileRepository.count();
        long totalFans = userRepository.countByRole(UserAccount.UserRole.USER);

        List<Payment> allPayments = paymentRepository.findAll();
        long successPayments = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCEEDED).count();
        BigDecimal totalRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCEEDED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal defaultRate = getDefaultCommissionRate();
        BigDecimal totalCommission = totalRevenue.multiply(defaultRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal artistEarnings = totalRevenue.subtract(totalCommission);

        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.PENDING).count();
        long completedBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED).count();

        stats.put("totalUsers", totalUsers);
        stats.put("totalArtists", totalArtists);
        stats.put("totalFans", totalFans);
        stats.put("totalBookings", totalBookings);
        stats.put("pendingBookings", pendingBookings);
        stats.put("completedBookings", completedBookings);
        stats.put("totalPayments", successPayments);
        stats.put("totalRevenue", totalRevenue);
        stats.put("platformCommission", totalCommission);
        stats.put("artistEarnings", artistEarnings);
        stats.put("defaultCommissionRate", defaultRate);

        // Verification stats
        long pendingVerifications = artistProfileRepository
                .countByVerificationStatus(ArtistProfile.VerificationStatus.PENDING_VERIFICATION);
        stats.put("pendingVerifications", pendingVerifications);

        return stats;
    }

    // ─────────────────────────────── USERS ───────────────────────────────

    public Page<Map<String, Object>> listAllUsers(String role, String status, Pageable pageable) {
        Page<UserAccount> users;
        if (role != null && status != null) {
            users = userRepository.findByRoleAndStatus(
                    UserAccount.UserRole.valueOf(role), UserAccount.UserStatus.valueOf(status), pageable);
        } else if (role != null) {
            users = userRepository.findByRole(UserAccount.UserRole.valueOf(role), pageable);
        } else if (status != null) {
            users = userRepository.findByStatus(UserAccount.UserStatus.valueOf(status), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        List<Map<String, Object>> dtos = users.getContent().stream().map(u -> {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", u.getId());
            dto.put("email", u.getEmail());
            dto.put("role", u.getRole().name());
            dto.put("status", u.getStatus().name());
            dto.put("phoneNumber", u.getPhoneNumber());
            dto.put("whatsappOptIn", u.getWhatsappOptIn());
            dto.put("createdAt", u.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, users.getTotalElements());
    }

    @Transactional
    public Map<String, Object> updateUserStatus(UUID userId, String newStatus) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserAccount.UserStatus statusEnum = UserAccount.UserStatus.valueOf(newStatus.toUpperCase());
        user.setStatus(statusEnum);
        userRepository.save(user);

        notificationService.send(userId, "ACCOUNT",
                "Account Status Updated",
                "Your account has been " + statusEnum.name().toLowerCase() + " by admin.");

        log.info("Admin updated user={} status to {}", userId, statusEnum);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userId", userId);
        result.put("newStatus", statusEnum.name());
        return result;
    }

    // ─────────────────────────────── ARTISTS ───────────────────────────────

    public Page<Map<String, Object>> listAllArtists(Pageable pageable) {
        Page<ArtistProfile> artists = artistProfileRepository.findAll(pageable);

        List<Map<String, Object>> dtos = artists.getContent().stream().map(a -> {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", a.getId());
            dto.put("userId", a.getUserId());
            dto.put("displayName", a.getDisplayName());
            dto.put("bio", a.getBio());
            dto.put("avatarUrl", a.getAvatarUrl());
            dto.put("featured", a.getFeatured());
            dto.put("ratingAggregate", a.getRatingAggregate());
            dto.put("ratingCount", a.getRatingCount());
            dto.put("createdAt", a.getCreatedAt());
            dto.put("verificationStatus", a.getVerificationStatus() != null ? a.getVerificationStatus().name() : "VERIFIED");
            dto.put("instagramUrl", a.getInstagramUrl());
            dto.put("youtubeUrl", a.getYoutubeUrl());
            dto.put("certificateInfo", a.getCertificateInfo());
            dto.put("rejectionReason", a.getRejectionReason());

            // Categories
            List<String> categories = categoryRepository.findByArtistId(a.getId())
                    .stream().map(ArtistCategory::getCategory).collect(Collectors.toList());
            dto.put("categories", categories);

            // Email
            userRepository.findById(a.getUserId()).ifPresent(u -> {
                dto.put("email", u.getEmail());
                dto.put("accountStatus", u.getStatus().name());
                dto.put("phoneNumber", u.getPhoneNumber());
            });

            // Offerings count + total earnings
            List<Offering> offerings = offeringRepository.findByArtistIdOrderByCreatedAtDesc(a.getId());
            dto.put("offeringsCount", offerings.size());
            dto.put("activeOfferingsCount", offerings.stream().filter(Offering::getActive).count());

            // Commission rate
            BigDecimal rate = commissionConfigRepository.findByArtistId(a.getId())
                    .map(CommissionConfig::getCommissionRate)
                    .orElse(getDefaultCommissionRate());
            dto.put("commissionRate", rate);

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, artists.getTotalElements());
    }

    @Transactional
    public Map<String, Object> toggleFeatured(UUID artistId) {
        ArtistProfile artist = artistProfileRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        artist.setFeatured(!Boolean.TRUE.equals(artist.getFeatured()));
        artistProfileRepository.save(artist);

        log.info("Admin toggled featured for artist={}, now={}", artistId, artist.getFeatured());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("artistId", artistId);
        result.put("featured", artist.getFeatured());
        return result;
    }

    // ─────────────────────────────── BOOKINGS ───────────────────────────────

    public Page<Map<String, Object>> listAllBookings(String status, Pageable pageable) {
        Page<Booking> bookings;
        if (status != null) {
            bookings = bookingRepository.findAll(pageable); // will filter below
        } else {
            bookings = bookingRepository.findAll(pageable);
        }

        List<Map<String, Object>> dtos = bookings.getContent().stream()
                .filter(b -> status == null || b.getStatus().name().equalsIgnoreCase(status))
                .map(b -> {
                    Map<String, Object> dto = new LinkedHashMap<>();
                    dto.put("id", b.getId());
                    dto.put("offeringId", b.getOfferingId());
                    dto.put("artistId", b.getArtistId());
                    dto.put("userId", b.getUserId());
                    dto.put("scheduledAt", b.getScheduledAt());
                    dto.put("durationMinutes", b.getDurationMinutes());
                    dto.put("status", b.getStatus().name());
                    dto.put("notes", b.getNotes());
                    dto.put("createdAt", b.getCreatedAt());

                    // Enrich with names
                    userRepository.findById(b.getUserId()).ifPresent(u ->
                            dto.put("fanEmail", u.getEmail()));
                    artistProfileRepository.findById(b.getArtistId()).ifPresent(a ->
                            dto.put("artistName", a.getDisplayName()));
                    offeringRepository.findById(b.getOfferingId()).ifPresent(o -> {
                        dto.put("offeringTitle", o.getTitle());
                        dto.put("offeringType", o.getType().name());
                        dto.put("offeringPrice", o.getPrice());
                    });

                    // Payment info
                    paymentRepository.findByBookingId(b.getId()).ifPresent(p -> {
                        dto.put("paymentStatus", p.getStatus().name());
                        dto.put("paymentAmount", p.getAmount());
                    });

                    return dto;
                }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, bookings.getTotalElements());
    }

    // ─────────────────────────────── PAYMENTS ───────────────────────────────

    public Page<Map<String, Object>> listAllPayments(String status, Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);

        List<Map<String, Object>> dtos = payments.getContent().stream()
                .filter(p -> status == null || p.getStatus().name().equalsIgnoreCase(status))
                .map(p -> {
                    Map<String, Object> dto = new LinkedHashMap<>();
                    dto.put("id", p.getId());
                    dto.put("bookingId", p.getBookingId());
                    dto.put("provider", p.getProvider());
                    dto.put("intentId", p.getIntentId());
                    dto.put("amount", p.getAmount());
                    dto.put("currency", p.getCurrency());
                    dto.put("status", p.getStatus().name());
                    dto.put("createdAt", p.getCreatedAt());

                    // Enrich with booking info
                    bookingRepository.findById(p.getBookingId()).ifPresent(b -> {
                        dto.put("bookingStatus", b.getStatus().name());
                        userRepository.findById(b.getUserId()).ifPresent(u ->
                                dto.put("fanEmail", u.getEmail()));
                        artistProfileRepository.findById(b.getArtistId()).ifPresent(a ->
                                dto.put("artistName", a.getDisplayName()));

                        // Calculate commission
                        BigDecimal rate = commissionConfigRepository.findByArtistId(b.getArtistId())
                                .map(CommissionConfig::getCommissionRate)
                                .orElse(getDefaultCommissionRate());
                        BigDecimal commission = p.getAmount().multiply(rate)
                                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                        BigDecimal artistPayout = p.getAmount().subtract(commission);
                        dto.put("commissionRate", rate);
                        dto.put("commissionAmount", commission);
                        dto.put("artistPayout", artistPayout);
                    });

                    return dto;
                }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, payments.getTotalElements());
    }

    // ─────────────────────────────── COMMISSION ───────────────────────────────

    public BigDecimal getDefaultCommissionRate() {
        return commissionConfigRepository.findByArtistIdIsNull()
                .map(CommissionConfig::getCommissionRate)
                .orElse(new BigDecimal("10.00"));
    }

    @Transactional
    public Map<String, Object> setDefaultCommissionRate(BigDecimal rate) {
        CommissionConfig config = commissionConfigRepository.findByArtistIdIsNull()
                .orElse(CommissionConfig.builder().build());
        config.setCommissionRate(rate);
        commissionConfigRepository.save(config);

        log.info("Admin set default commission rate to {}%", rate);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("defaultRate", rate);
        return result;
    }

    @Transactional
    public Map<String, Object> setArtistCommissionRate(UUID artistId, BigDecimal rate) {
        artistProfileRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        CommissionConfig config = commissionConfigRepository.findByArtistId(artistId)
                .orElse(CommissionConfig.builder().artistId(artistId).build());
        config.setCommissionRate(rate);
        commissionConfigRepository.save(config);

        log.info("Admin set commission rate for artist={} to {}%", artistId, rate);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("artistId", artistId);
        result.put("commissionRate", rate);
        return result;
    }

    public List<Map<String, Object>> listAllCommissions() {
        List<CommissionConfig> configs = commissionConfigRepository.findAll();
        return configs.stream().map(c -> {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", c.getId());
            dto.put("artistId", c.getArtistId());
            dto.put("commissionRate", c.getCommissionRate());
            dto.put("isDefault", c.getArtistId() == null);
            if (c.getArtistId() != null) {
                artistProfileRepository.findById(c.getArtistId()).ifPresent(a ->
                        dto.put("artistName", a.getDisplayName()));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // ─────────────────────────────── ARTIST VERIFICATION ───────────────────────────────

    public Page<Map<String, Object>> listPendingVerifications(Pageable pageable) {
        Page<ArtistProfile> pending = artistProfileRepository
                .findByVerificationStatus(ArtistProfile.VerificationStatus.PENDING_VERIFICATION, pageable);

        List<Map<String, Object>> dtos = pending.getContent().stream().map(a -> {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", a.getId());
            dto.put("userId", a.getUserId());
            dto.put("displayName", a.getDisplayName());
            dto.put("bio", a.getBio());
            dto.put("avatarUrl", a.getAvatarUrl());
            dto.put("instagramUrl", a.getInstagramUrl());
            dto.put("youtubeUrl", a.getYoutubeUrl());
            dto.put("certificateInfo", a.getCertificateInfo());
            dto.put("verificationStatus", a.getVerificationStatus().name());
            dto.put("createdAt", a.getCreatedAt());

            userRepository.findById(a.getUserId()).ifPresent(u -> {
                dto.put("email", u.getEmail());
                dto.put("phoneNumber", u.getPhoneNumber());
            });

            List<String> categories = categoryRepository.findByArtistId(a.getId())
                    .stream().map(ArtistCategory::getCategory).collect(Collectors.toList());
            dto.put("categories", categories);

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, pending.getTotalElements());
    }

    @Transactional
    public Map<String, Object> verifyArtist(UUID artistId, UUID adminUserId) {
        ArtistProfile artist = artistProfileRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        artist.setVerificationStatus(ArtistProfile.VerificationStatus.VERIFIED);
        artist.setVerifiedAt(LocalDateTime.now());
        artist.setVerifiedBy(adminUserId);
        artist.setRejectionReason(null);
        artistProfileRepository.save(artist);

        // Notify the artist
        notificationService.send(artist.getUserId(), "VERIFICATION",
                "Profile Verified! ✅",
                "Congratulations! Your artist profile has been verified. You are now visible to fans on TopConnect.");

        log.info("Admin {} verified artist {}", adminUserId, artistId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("artistId", artistId);
        result.put("displayName", artist.getDisplayName());
        result.put("verificationStatus", "VERIFIED");
        return result;
    }

    @Transactional
    public Map<String, Object> rejectArtist(UUID artistId, UUID adminUserId, String reason) {
        ArtistProfile artist = artistProfileRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        artist.setVerificationStatus(ArtistProfile.VerificationStatus.REJECTED);
        artist.setRejectionReason(reason);
        artist.setVerifiedBy(adminUserId);
        artistProfileRepository.save(artist);

        // Notify the artist
        notificationService.send(artist.getUserId(), "VERIFICATION",
                "Verification Rejected",
                "Your artist verification was not approved. Reason: " + reason
                        + ". You can update your profile and resubmit.");

        log.info("Admin {} rejected artist {} — reason: {}", adminUserId, artistId, reason);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("artistId", artistId);
        result.put("displayName", artist.getDisplayName());
        result.put("verificationStatus", "REJECTED");
        result.put("reason", reason);
        return result;
    }
}

