package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.BookingDTO;
import org.connect.dto.ApiResponse;
import org.connect.security.SecurityUtils;
import org.connect.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bookings", description = "Booking management endpoints")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking")
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@Valid @RequestBody BookingDTO request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Creating booking for user={}, offering={}", userId, request.getOfferingId());
        BookingDTO booking = bookingService.createBooking(userId, request.getOfferingId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Booking created", booking));
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking details")
    public ResponseEntity<ApiResponse<BookingDTO>> getBooking(@PathVariable UUID bookingId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        BookingDTO booking = bookingService.getBooking(bookingId, userId);
        return ResponseEntity.ok(new ApiResponse<>("Booking retrieved", booking));
    }

    @GetMapping
    @Operation(summary = "List user bookings")
    public ResponseEntity<ApiResponse<Page<BookingDTO>>> getUserBookings(Pageable pageable) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Fetching bookings for user={}", userId);
        Page<BookingDTO> bookings = bookingService.getUserBookings(userId, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Bookings retrieved", bookings));
    }

    @PostMapping("/{bookingId}/confirm")
    @Operation(summary = "Confirm booking (artist only)")
    public ResponseEntity<ApiResponse<Map<String, String>>> confirmBooking(@PathVariable UUID bookingId) {
        UUID artistId = SecurityUtils.getCurrentUserId();
        log.info("Confirming booking={} by artist={}", bookingId, artistId);
        bookingService.confirmBooking(bookingId, artistId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking confirmed");
        return ResponseEntity.ok(new ApiResponse<>("Booking confirmed", response));
    }

    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel booking")
    public ResponseEntity<ApiResponse<Map<String, String>>> cancelBooking(
            @PathVariable UUID bookingId,
            @RequestParam(required = false) String reason) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Cancelling booking={} by user={}", bookingId, userId);
        bookingService.cancelBooking(bookingId, userId, reason != null ? reason : "Cancelled by user");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking cancelled");
        return ResponseEntity.ok(new ApiResponse<>("Booking cancelled", response));
    }

    @PostMapping("/{bookingId}/complete")
    @Operation(summary = "Mark booking as completed (artist)")
    public ResponseEntity<ApiResponse<BookingDTO>> completeBooking(@PathVariable UUID bookingId) {
        UUID artistId = SecurityUtils.getCurrentUserId();
        log.info("Completing booking={} by artist={}", bookingId, artistId);
        BookingDTO booking = bookingService.completeBooking(bookingId, artistId);
        return ResponseEntity.ok(new ApiResponse<>("Booking completed", booking));
    }
}