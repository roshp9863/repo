package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ApiResponse;
import org.connect.entity.ArtistProfile;
import org.connect.entity.Offering;
import org.connect.entity.UserAccount;
import org.connect.repository.ArtistProfileRepository;
import org.connect.repository.OfferingRepository;
import org.connect.repository.UserAccountRepository;
import org.connect.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin", description = "Admin management endpoints")
public class AdminController {

    private final UserAccountRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final OfferingRepository offeringRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;

    // ══════════════════════════ ADMIN DASHBOARD ══════════════════════════

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get admin dashboard stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        log.info("Admin fetching dashboard stats");
        Map<String, Object> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(new ApiResponse<>("Dashboard stats", stats));
    }

    // ══════════════════════════ USER MANAGEMENT ══════════════════════════

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users with optional role/status filter")
    public ResponseEntity<ApiResponse<Page<Map<String, Object>>>> listUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        log.info("Admin listing users, role={}, status={}", role, status);
        Page<Map<String, Object>> users = adminService.listAllUsers(role, status, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Users retrieved", users));
    }

    @PutMapping("/users/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user status (ACTIVE, SUSPENDED, DELETED)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateUserStatus(
            @PathVariable UUID userId,
            @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        if (newStatus == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("'status' is required", null));
        }
        log.info("Admin updating user={} status to {}", userId, newStatus);
        Map<String, Object> result = adminService.updateUserStatus(userId, newStatus);
        return ResponseEntity.ok(new ApiResponse<>("User status updated", result));
    }

    // ══════════════════════════ ARTIST MANAGEMENT ══════════════════════════

    @GetMapping("/artists")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all artists with full details")
    public ResponseEntity<ApiResponse<Page<Map<String, Object>>>> listArtists(Pageable pageable) {
        log.info("Admin listing artists");
        Page<Map<String, Object>> artists = adminService.listAllArtists(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Artists retrieved", artists));
    }

    @PostMapping("/artists/{artistId}/toggle-featured")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle artist featured status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleFeatured(@PathVariable UUID artistId) {
        log.info("Admin toggling featured for artist={}", artistId);
        Map<String, Object> result = adminService.toggleFeatured(artistId);
        return ResponseEntity.ok(new ApiResponse<>("Featured status toggled", result));
    }

    // ══════════════════════════ BOOKING MANAGEMENT ══════════════════════════

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all bookings")
    public ResponseEntity<ApiResponse<Page<Map<String, Object>>>> listBookings(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        log.info("Admin listing bookings, status={}", status);
        Page<Map<String, Object>> bookings = adminService.listAllBookings(status, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Bookings retrieved", bookings));
    }

    // ══════════════════════════ PAYMENT MANAGEMENT ══════════════════════════

    @GetMapping("/payments")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all payments with commission breakdown")
    public ResponseEntity<ApiResponse<Page<Map<String, Object>>>> listPayments(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        log.info("Admin listing payments, status={}", status);
        Page<Map<String, Object>> payments = adminService.listAllPayments(status, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Payments retrieved", payments));
    }

    // ══════════════════════════ COMMISSION MANAGEMENT ══════════════════════════

    @GetMapping("/commissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all commission configurations")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listCommissions() {
        List<Map<String, Object>> commissions = adminService.listAllCommissions();
        return ResponseEntity.ok(new ApiResponse<>("Commissions retrieved", commissions));
    }

    @PutMapping("/commissions/default")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set default platform commission rate (%)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> setDefaultRate(
            @RequestBody Map<String, Object> request) {
        BigDecimal rate = new BigDecimal(request.get("rate").toString());
        if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(new BigDecimal("100")) > 0) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Rate must be 0-100", null));
        }
        log.info("Admin setting default commission rate to {}%", rate);
        Map<String, Object> result = adminService.setDefaultCommissionRate(rate);
        return ResponseEntity.ok(new ApiResponse<>("Default commission updated", result));
    }

    @PutMapping("/commissions/artist/{artistId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set commission rate for a specific artist")
    public ResponseEntity<ApiResponse<Map<String, Object>>> setArtistRate(
            @PathVariable UUID artistId,
            @RequestBody Map<String, Object> request) {
        BigDecimal rate = new BigDecimal(request.get("rate").toString());
        if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(new BigDecimal("100")) > 0) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Rate must be 0-100", null));
        }
        log.info("Admin setting commission for artist={} to {}%", artistId, rate);
        Map<String, Object> result = adminService.setArtistCommissionRate(artistId, rate);
        return ResponseEntity.ok(new ApiResponse<>("Artist commission updated", result));
    }

    // ══════════════════════════ SEED TEST DATA (DEV) ══════════════════════════

    @GetMapping("/seed-test-data")
    @Operation(summary = "Seed database with test data (DEV ONLY)")
    public ResponseEntity<Map<String, Object>> seedTestData() {
        try {
            Map<String, Object> response = new HashMap<>();
            int usersVerified = verifyPendingUsers();
            response.put("users_verified", usersVerified);
            int artistsAdded = addTestArtists();
            response.put("artists_added", artistsAdded);
            int servicesAdded = addTestServices();
            response.put("services_added", servicesAdded);
            boolean fanAdded = addFanUser();
            response.put("fan_user_added", fanAdded);
            boolean adminAdded = ensureAdminUser();
            response.put("admin_user_added", adminAdded);
            response.put("status", "success");
            response.put("message", "Test data seeded successfully");
            log.info("Test data seeded: {} users verified, {} artists added, {} services added",
                    usersVerified, artistsAdded, servicesAdded);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to seed test data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    private boolean ensureAdminUser() {
        if (!userRepository.existsByEmail("admin@topconnect.com")) {
            UserAccount admin = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .email("admin@topconnect.com")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role(UserAccount.UserRole.ADMIN)
                    .status(UserAccount.UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);
            return true;
        }
        return false;
    }

    private int verifyPendingUsers() {
        var pendingUsers = userRepository.findAll().stream()
                .filter(u -> u.getStatus() == UserAccount.UserStatus.PENDING).toList();
        for (UserAccount user : pendingUsers) {
            user.setStatus(UserAccount.UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return pendingUsers.size();
    }

    private int addTestArtists() {
        int count = 0;
        String[][] artists = {
            {"john.producer@test.com", "John Producer", "Professional music producer with 10 years of experience.", "4.8", "25"},
            {"sarah.vocalist@test.com", "Sarah Vocalist", "Versatile vocalist and vocal coach.", "4.9", "32"},
            {"mike.guitarist@test.com", "Mike Guitarist", "Expert guitarist specializing in rock, blues, and acoustic.", "4.6", "18"},
            {"emma.engineer@test.com", "Emma Sound Engineer", "Professional sound engineer and mixing specialist.", "4.7", "22"},
            {"david.beatmaker@test.com", "David Beatmaker", "Create custom beats and instrumentals.", "4.5", "15"},
        };
        for (String[] a : artists) {
            if (!userRepository.existsByEmail(a[0])) {
                UserAccount user = userRepository.save(UserAccount.builder()
                        .email(a[0]).passwordHash(passwordEncoder.encode("test123"))
                        .role(UserAccount.UserRole.ARTIST).status(UserAccount.UserStatus.ACTIVE).build());
                artistProfileRepository.save(ArtistProfile.builder()
                        .userId(user.getId()).displayName(a[1]).bio(a[2])
                        .timezone("UTC").featured(count < 3)
                        .ratingAggregate(new BigDecimal(a[3])).ratingCount(Integer.parseInt(a[4]))
                        .avatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=" + a[1].split(" ")[0]).build());
                count++;
            }
        }
        return count;
    }

    private int addTestServices() {
        int count = 0;
        for (ArtistProfile artist : artistProfileRepository.findAll()) {
            if (offeringRepository.findByArtistIdOrderByCreatedAtDesc(artist.getId()).isEmpty()) {
                offeringRepository.save(Offering.builder()
                        .artistId(artist.getId()).title("Video Consultation").description("1-on-1 video session")
                        .type(Offering.OfferingType.VIDEO).price(BigDecimal.valueOf(100)).currency("USD").durationMinutes(60).build());
                offeringRepository.save(Offering.builder()
                        .artistId(artist.getId()).title("Chat Session").description("Text-based Q&A")
                        .type(Offering.OfferingType.CHAT).price(BigDecimal.valueOf(30)).currency("USD").durationMinutes(30).build());
                count += 2;
            }
        }
        return count;
    }

    private boolean addFanUser() {
        if (!userRepository.existsByEmail("fan.user@test.com")) {
            userRepository.save(UserAccount.builder()
                    .email("fan.user@test.com").passwordHash(passwordEncoder.encode("test123"))
                    .role(UserAccount.UserRole.USER).status(UserAccount.UserStatus.ACTIVE).build());
            return true;
        }
        return false;
    }

    // ══════════════════════════ ARTIST VERIFICATION ══════════════════════════

    @GetMapping("/verifications/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List artists pending verification")
    public ResponseEntity<ApiResponse<Page<Map<String, Object>>>> listPendingVerifications(Pageable pageable) {
        log.info("Admin listing pending verifications");
        Page<Map<String, Object>> pending = adminService.listPendingVerifications(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Pending verifications", pending));
    }

    @PostMapping("/verifications/{artistId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Verify (approve) an artist")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyArtist(
            @PathVariable UUID artistId,
            @org.springframework.security.core.annotation.AuthenticationPrincipal
            org.connect.security.UserPrincipal principal) {
        log.info("Admin {} verifying artist {}", principal.getId(), artistId);
        Map<String, Object> result = adminService.verifyArtist(artistId, principal.getId());
        return ResponseEntity.ok(new ApiResponse<>("Artist verified successfully", result));
    }

    @PostMapping("/verifications/{artistId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reject an artist verification")
    public ResponseEntity<ApiResponse<Map<String, Object>>> rejectArtist(
            @PathVariable UUID artistId,
            @RequestBody Map<String, String> request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal
            org.connect.security.UserPrincipal principal) {
        String reason = request.get("reason");
        if (reason == null || reason.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Rejection reason is required", null));
        }
        log.info("Admin {} rejecting artist {} — reason: {}", principal.getId(), artistId, reason);
        Map<String, Object> result = adminService.rejectArtist(artistId, principal.getId(), reason);
        return ResponseEntity.ok(new ApiResponse<>("Artist rejected", result));
    }
}

