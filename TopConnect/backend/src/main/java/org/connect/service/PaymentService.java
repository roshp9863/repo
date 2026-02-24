package org.connect.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.PaymentIntentDTO;
import org.connect.entity.Booking;
import org.connect.entity.Offering;
import org.connect.entity.Payment;
import org.connect.repository.BookingRepository;
import org.connect.repository.OfferingRepository;
import org.connect.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final OfferingRepository offeringRepository;

    @Value("${stripe.api-key:sk_test_placeholder}")
    private String stripeApiKey;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${app.payment.dev-mode:true}")
    private boolean devMode;

    public PaymentIntentDTO createCheckoutSession(UUID bookingId, UUID userId) throws StripeException {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or access denied"));

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new IllegalArgumentException("Cannot create payment for non-pending booking");
        }

        // Check if payment already exists
        Payment existingPayment = paymentRepository.findByBookingId(bookingId).orElse(null);
        if (existingPayment != null && existingPayment.getStatus() == Payment.PaymentStatus.SUCCEEDED) {
            throw new IllegalArgumentException("Payment already completed for this booking");
        }

        // Fetch the offering to get the actual price
        Offering offering = offeringRepository.findById(booking.getOfferingId())
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        BigDecimal amount = offering.getPrice();

        // Dev mode: skip Stripe, create a mock payment and auto-confirm
        if (devMode || stripeApiKey.contains("placeholder")) {
            log.info("DEV MODE: Creating mock payment for booking: {}", bookingId);

            Payment payment = existingPayment != null ? existingPayment : Payment.builder()
                    .id(UUID.randomUUID())
                    .bookingId(bookingId)
                    .provider("DEV_MODE")
                    .intentId("dev_" + UUID.randomUUID().toString())
                    .amount(amount)
                    .currency(offering.getCurrency() != null ? offering.getCurrency() : "USD")
                    .status(Payment.PaymentStatus.SUCCEEDED)
                    .build();

            if (existingPayment == null) {
                payment = paymentRepository.save(payment);
            } else {
                payment.setStatus(Payment.PaymentStatus.SUCCEEDED);
                payment = paymentRepository.save(payment);
            }

            // Auto-confirm booking
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            log.info("DEV MODE: Payment auto-confirmed for booking: {}", bookingId);

            return PaymentIntentDTO.builder()
                    .sessionId(payment.getIntentId())
                    .clientSecret("dev_mode_no_secret")
                    .publishableKey("dev_mode")
                    .build();
        }

        // Production mode: use Stripe
        Stripe.apiKey = stripeApiKey;

        Payment payment = existingPayment != null ? existingPayment : Payment.builder()
                .id(UUID.randomUUID())
                .bookingId(bookingId)
                .provider("STRIPE")
                .amount(amount)
                .currency(offering.getCurrency() != null ? offering.getCurrency() : "USD")
                .status(Payment.PaymentStatus.PENDING)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/booking/" + bookingId + "?payment=success")
                .setCancelUrl(frontendUrl + "/booking/" + bookingId + "?payment=cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(payment.getCurrency().toLowerCase())
                                                .setUnitAmount(payment.getAmount().longValue() * 100)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(offering.getTitle())
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);

        payment.setIntentId(session.getId());
        payment = paymentRepository.save(payment);

        log.info("Checkout session created: {} for booking: {}", session.getId(), bookingId);

        return PaymentIntentDTO.builder()
                .sessionId(session.getId())
                .clientSecret(session.getClientSecret())
                .publishableKey("pk_test_your_publishable_key")
                .build();
    }

    public void confirmPayment(String sessionId) throws StripeException {
        if (devMode) {
            log.info("DEV MODE: confirmPayment called for session: {}", sessionId);
            return;
        }

        Stripe.apiKey = stripeApiKey;
        Session session = Session.retrieve(sessionId);

        Payment payment = paymentRepository.findByIntentId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (session.getPaymentStatus().equals("paid")) {
            payment.setStatus(Payment.PaymentStatus.SUCCEEDED);
            paymentRepository.save(payment);

            Booking booking = bookingRepository.findById(payment.getBookingId())
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            log.info("Payment confirmed: {}", sessionId);
        }
    }

    public Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    public Payment getPaymentByBookingId(UUID bookingId) {
        return paymentRepository.findByBookingId(bookingId).orElse(null);
    }

}
