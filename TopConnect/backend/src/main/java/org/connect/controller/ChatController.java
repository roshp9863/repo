package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ChatMessageDTO;
import org.connect.dto.ApiResponse;
import org.connect.security.SecurityUtils;
import org.connect.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "Chat messaging endpoints")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @Operation(summary = "List user chat threads")
    public ResponseEntity<ApiResponse<Object>> listChats(Pageable pageable) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.debug("Listing chats for user={}", userId);
        var threads = chatService.getUserChatDTOs(userId, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Chats retrieved", threads));
    }

    @PostMapping("/thread")
    @Operation(summary = "Create or get a chat thread with an artist")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createOrGetThread(
            @RequestBody Map<String, String> request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        String artistProfileIdStr = request.get("artistProfileId");
        if (artistProfileIdStr == null || artistProfileIdStr.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("artistProfileId is required", null));
        }
        UUID artistProfileId = UUID.fromString(artistProfileIdStr);
        log.info("Creating/getting chat thread: user={}, artist={}", userId, artistProfileId);
        var thread = chatService.createOrGetThread(artistProfileId, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("threadId", thread.getId());
        response.put("artistId", thread.getArtistId());
        response.put("userId", thread.getUserId());
        return ResponseEntity.ok(new ApiResponse<>("Thread retrieved", response));
    }

    @GetMapping("/{threadId}/messages")
    @Operation(summary = "Get messages in a chat thread")
    public ResponseEntity<ApiResponse<Page<ChatMessageDTO>>> getMessages(
            @PathVariable UUID threadId,
            Pageable pageable) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Page<ChatMessageDTO> messages = chatService.getThreadMessages(threadId, userId, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Messages retrieved", messages));
    }

    @PostMapping("/{threadId}/messages")
    @Operation(summary = "Send a message in chat thread")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> sendMessage(
            @PathVariable UUID threadId,
            @RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Message cannot be empty", null));
        }
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Sending message in thread={} by user={}", threadId, userId);
        var msg = chatService.sendMessage(threadId, userId, message);
        ChatMessageDTO dto = ChatMessageDTO.builder()
                .id(msg.getId())
                .threadId(msg.getThreadId())
                .senderId(msg.getSenderId())
                .message(msg.getMessage())
                .createdAt(msg.getCreatedAt())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Message sent", dto));
    }

    @PostMapping("/{threadId}/mark-read")
    @Operation(summary = "Mark all messages as read")
    public ResponseEntity<ApiResponse<Map<String, String>>> markAsRead(@PathVariable UUID threadId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.debug("Marking messages as read in thread={} for user={}", threadId, userId);
        chatService.markMessagesAsRead(threadId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Messages marked as read");
        return ResponseEntity.ok(new ApiResponse<>("Messages marked as read", response));
    }
}