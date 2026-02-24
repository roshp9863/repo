package org.connect.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ApiResponse;
import org.connect.dto.ChatMessageDTO;
import org.connect.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Webhook endpoint for receiving WhatsApp replies from Twilio.
 *
 * Twilio sends POST requests with form-encoded data when a user replies
 * to a WhatsApp message. Key fields:
 *   - From: whatsapp:+1234567890 (the artist's phone number)
 *   - Body: the reply text
 *
 * This endpoint is unauthenticated (exempted in SecurityConfig).
 *
 * For local testing without Twilio:
 *   curl -X POST http://localhost:8080/api/v1/webhooks/whatsapp \
 *     -H "Content-Type: application/x-www-form-urlencoded" \
 *     -d "From=whatsapp:+1234567890&Body=Hello from WhatsApp"
 *
 * Or use the JSON test endpoint:
 *   curl -X POST http://localhost:8080/api/v1/webhooks/whatsapp/test \
 *     -H "Content-Type: application/json" \
 *     -d '{"phoneNumber":"+1234567890","message":"Hello from WhatsApp"}'
 */
@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WhatsAppWebhookController {

    private final ChatService chatService;

    /**
     * Twilio WhatsApp webhook — receives form-encoded POST from Twilio.
     */
    @PostMapping(value = "/whatsapp", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> handleTwilioWebhook(
            @RequestParam("From") String from,
            @RequestParam("Body") String body) {

        log.info("WhatsApp webhook received: From={}, Body={}", from,
                body.length() > 50 ? body.substring(0, 50) + "..." : body);

        try {
            chatService.saveWhatsAppReply(from, body);
            // Return TwiML empty response (Twilio expects XML)
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body("<Response></Response>");
        } catch (Exception e) {
            log.error("WhatsApp webhook processing failed: {}", e.getMessage());
            // Still return 200 to Twilio so it doesn't retry
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body("<Response></Response>");
        }
    }

    /**
     * JSON test endpoint for local dev — simulates a WhatsApp reply.
     * Use this to test without Twilio:
     *   POST /api/v1/webhooks/whatsapp/test
     *   { "phoneNumber": "+1234567890", "message": "Hello" }
     */
    @PostMapping("/whatsapp/test")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> handleTestWebhook(
            @RequestBody Map<String, String> request) {

        String phoneNumber = request.get("phoneNumber");
        String message = request.get("message");

        if (phoneNumber == null || message == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("phoneNumber and message are required", null));
        }

        log.info("WhatsApp TEST webhook: phone={}, message={}", phoneNumber, message);

        try {
            ChatMessageDTO saved = chatService.saveWhatsAppReply(phoneNumber, message);
            return ResponseEntity.ok(new ApiResponse<>("WhatsApp reply saved", saved));
        } catch (Exception e) {
            log.error("WhatsApp test webhook failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}

