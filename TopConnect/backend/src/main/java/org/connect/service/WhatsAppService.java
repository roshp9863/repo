package org.connect.service;

import lombok.extern.slf4j.Slf4j;
import org.connect.entity.UserAccount;
import org.connect.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

/**
 * WhatsApp notification service.
 *
 * In dev mode, notifications are logged. In production, integrate Twilio:
 *   - Add com.twilio.sdk:twilio dependency
 *   - Set twilio.account-sid, twilio.auth-token, twilio.whatsapp-from in application.yml
 *   - Use Twilio's Message.creator() API with "whatsapp:" prefix
 *
 * Twilio WhatsApp requires a verified Business Account and pre-approved templates.
 * Sandbox: whatsapp:+14155238886 (for testing)
 */
@Service
@Slf4j
public class WhatsAppService {

    private final UserAccountRepository userAccountRepository;

    @Value("${twilio.enabled:false}")
    private boolean twilioEnabled;

    @Value("${twilio.whatsapp-from:whatsapp:+14155238886}")
    private String whatsappFrom;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    public WhatsAppService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public void notifyBookingCreated(UUID fanUserId, UUID artistUserId, String offeringTitle, LocalDateTime scheduledAt) {
        sendToUser(fanUserId, String.format(
                "🎯 *Booking Confirmed!*\n\nYour session \"%s\" is scheduled for %s.\n\nOpen TopConnect to view details.",
                offeringTitle, scheduledAt != null ? scheduledAt.format(FORMATTER) : "TBD"));

        sendToUser(artistUserId, String.format(
                "📅 *New Booking!*\n\nYou have a new booking for \"%s\" on %s.\n\nOpen TopConnect to manage it.",
                offeringTitle, scheduledAt != null ? scheduledAt.format(FORMATTER) : "TBD"));
    }

    public void notifyBookingConfirmed(UUID fanUserId, String offeringTitle, LocalDateTime scheduledAt) {
        sendToUser(fanUserId, String.format(
                "✅ *Booking Confirmed by Artist!*\n\nYour session \"%s\" on %s has been confirmed.\n\nSee you there!",
                offeringTitle, scheduledAt != null ? scheduledAt.format(FORMATTER) : "TBD"));
    }

    public void notifyBookingCancelled(UUID recipientUserId, String offeringTitle, String reason) {
        sendToUser(recipientUserId, String.format(
                "❌ *Booking Cancelled*\n\nThe session \"%s\" has been cancelled.\nReason: %s",
                offeringTitle, reason != null ? reason : "No reason provided"));
    }

    public void notifyPaymentReceived(UUID fanUserId, String offeringTitle, String amount) {
        sendToUser(fanUserId, String.format(
                "💳 *Payment Received!*\n\nPayment of %s for \"%s\" was successful. Thank you!",
                amount, offeringTitle));
    }

    public void notifyChatMessage(UUID artistUserId, String fanName, String message, UUID threadId) {
        String preview = message.length() > 200 ? message.substring(0, 200) + "..." : message;
        sendToUser(artistUserId, String.format(
                "💬 *New Message from %s*\n\n\"%s\"\n\n👉 *Reply directly to this message* and your reply will appear on TopConnect automatically.",
                fanName, preview));
    }

    private void sendToUser(UUID userId, String message) {
        try {
            Optional<UserAccount> userOpt = userAccountRepository.findById(userId);
            if (userOpt.isEmpty()) {
                log.debug("WhatsApp: User {} not found, skipping notification", userId);
                return;
            }

            UserAccount user = userOpt.get();
            if (!Boolean.TRUE.equals(user.getWhatsappOptIn()) || user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
                log.debug("WhatsApp: User {} has not opted in or has no phone number, skipping", userId);
                return;
            }

            if (twilioEnabled) {
                sendViaTwilio(user.getPhoneNumber(), message);
            } else {
                log.info("WhatsApp [DEV MODE] → to={} ({}): {}", user.getPhoneNumber(), user.getEmail(), message.replace("\n", " | "));
            }
        } catch (Exception e) {
            log.error("Failed to send WhatsApp notification to user={}: {}", userId, e.getMessage());
        }
    }

    private void sendViaTwilio(String phoneNumber, String message) {
        // TODO: Uncomment when com.twilio.sdk:twilio is added to pom.xml
        //
        // import com.twilio.Twilio;
        // import com.twilio.rest.api.v2010.account.Message;
        // import com.twilio.type.PhoneNumber;
        //
        // Twilio.init(accountSid, authToken);
        // Message.creator(
        //     new PhoneNumber("whatsapp:" + phoneNumber),
        //     new PhoneNumber(whatsappFrom),
        //     message
        // ).create();

        log.info("WhatsApp [TWILIO] → Sending to whatsapp:{}: {}", phoneNumber, message.replace("\n", " | "));
    }
}

