package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from:noreply@topconnect.local}")
    private String fromEmail;

    @Value("${app.email.name:TopConnect}")
    private String fromName;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Async
    public void sendVerificationEmail(String to, String token) {
        String verificationLink = frontendUrl + "/verify-email?token=" + token;
        String subject = "Verify your TopConnect account";
        String body = "Please verify your email by clicking this link:\n" + verificationLink
                + "\n\nThis link will expire in 24 hours.";

        sendEmail(to, subject, body);
    }

    @Async
    public void sendBookingConfirmedEmail(String to, String bookingDetails) {
        String subject = "Booking Confirmed - TopConnect";
        String body = "Your booking has been confirmed!\n\n" + bookingDetails;
        sendEmail(to, subject, body);
    }

    @Async
    public void sendPaymentReceiptEmail(String to, String paymentDetails) {
        String subject = "Payment Receipt - TopConnect";
        String body = "Payment received successfully!\n\n" + paymentDetails;
        sendEmail(to, subject, body);
    }

    @Async
    public void sendBookingCancelledEmail(String to, String reason) {
        String subject = "Booking Cancelled - TopConnect";
        String body = "Your booking has been cancelled.\n\nReason: " + reason;
        sendEmail(to, subject, body);
    }

    @Async
    public void sendNewMessageEmail(String to, String senderName) {
        String subject = "New message from " + senderName + " - TopConnect";
        String body = "You have a new message from " + senderName + ".\n\nLog in to TopConnect to view the message.";
        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }

}

