package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.PaymentIntentDTO;
import org.connect.dto.ApiResponse;
import org.connect.security.SecurityUtils;
import org.connect.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payments", description = "Payment and Stripe integration endpoints")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-checkout")
    @Operation(summary = "Create Stripe checkout session")
    public ResponseEntity<ApiResponse<PaymentIntentDTO>> createCheckoutSession(
            @RequestParam UUID bookingId) throws Exception {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Creating checkout session for booking={}, user={}", bookingId, userId);
        PaymentIntentDTO intent = paymentService.createCheckoutSession(bookingId, userId);
        return ResponseEntity.ok(new ApiResponse<>("Checkout session created", intent));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment status")
    public ResponseEntity<ApiResponse<Object>> getPayment(@PathVariable UUID paymentId) {
        log.debug("Fetching payment: {}", paymentId);
        var payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(new ApiResponse<>("Payment retrieved", payment));
    }

    @PostMapping("/webhook/stripe")
    @Operation(summary = "Stripe webhook handler")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload) {
        // Stripe webhook handler - for production, verify signature
        return ResponseEntity.ok("{\"received\": true}");
    }

}
