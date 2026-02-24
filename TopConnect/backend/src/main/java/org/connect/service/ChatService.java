package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ChatMessageDTO;
import org.connect.entity.ArtistProfile;
import org.connect.entity.ChatMessage;
import org.connect.entity.ChatThread;
import org.connect.repository.ArtistProfileRepository;
import org.connect.repository.ChatMessageRepository;
import org.connect.repository.ChatThreadRepository;
import org.connect.repository.UserAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatService {

    private final ChatThreadRepository threadRepository;
    private final ChatMessageRepository messageRepository;
    private final UserAccountRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final WhatsAppService whatsAppService;
    private final NotificationService notificationService;

    public ChatMessage sendMessage(UUID threadId, UUID senderId, String message) {
        ChatThread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Chat thread not found"));

        // Verify sender is part of the thread (check user ID directly or via artist profile)
        boolean isUser = thread.getUserId().equals(senderId);
        boolean isArtist = false;
        if (!isUser) {
            // Check if sender is the artist (senderId is user account ID, thread has artist profile ID)
            Optional<ArtistProfile> artistProfile = artistProfileRepository.findByUserId(senderId);
            isArtist = artistProfile.isPresent() && artistProfile.get().getId().equals(thread.getArtistId());
        }
        if (!isUser && !isArtist) {
            throw new IllegalArgumentException("Not authorized to send messages in this thread");
        }

        ChatMessage msg = ChatMessage.builder()
                .id(UUID.randomUUID())
                .threadId(threadId)
                .senderId(senderId)
                .message(message)
                .source("WEB")
                .build();

        msg = messageRepository.save(msg);
        thread.setUpdatedAt(LocalDateTime.now());
        threadRepository.save(thread);

        // If the sender is the fan, notify the artist via WhatsApp and in-app
        if (isUser) {
            try {
                ArtistProfile artist = artistProfileRepository.findById(thread.getArtistId()).orElse(null);
                if (artist != null) {
                    String fanEmail = userRepository.findById(senderId)
                            .map(u -> u.getEmail()).orElse("A fan");
                    whatsAppService.notifyChatMessage(artist.getUserId(), fanEmail, message, threadId);
                    notificationService.send(artist.getUserId(), "MESSAGE",
                            "New Message from " + fanEmail,
                            message.length() > 100 ? message.substring(0, 100) + "..." : message);
                }
            } catch (Exception e) {
                log.warn("Failed to notify artist of new message: {}", e.getMessage());
            }
        }

        log.info("Message sent in thread: {}", threadId);
        return msg;
    }

    public Page<ChatMessageDTO> getThreadMessages(UUID threadId, UUID userId, Pageable pageable) {
        ChatThread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Chat thread not found"));

        // Verify access (userId is user account ID; thread.artistId is artist profile ID)
        boolean isUser = thread.getUserId().equals(userId);
        boolean isArtist = false;
        if (!isUser) {
            Optional<ArtistProfile> artistProfile = artistProfileRepository.findByUserId(userId);
            isArtist = artistProfile.isPresent() && artistProfile.get().getId().equals(thread.getArtistId());
        }
        if (!isUser && !isArtist) {
            throw new IllegalArgumentException("Not authorized to view this thread");
        }

        Page<ChatMessage> messages = messageRepository.findByThreadIdOrderByCreatedAtDesc(threadId, pageable);

        return new PageImpl<>(
                messages.getContent().stream()
                        .map(this::toChatMessageDTO)
                        .collect(Collectors.toList()),
                pageable,
                messages.getTotalElements()
        );
    }

    public ChatThread createOrGetThread(UUID artistProfileId, UUID userId) {
        return threadRepository.findByArtistIdAndUserId(artistProfileId, userId)
                .orElseGet(() -> {
                    ChatThread thread = ChatThread.builder()
                            .id(UUID.randomUUID())
                            .artistId(artistProfileId)
                            .userId(userId)
                            .build();
                    return threadRepository.save(thread);
                });
    }

    public Page<Map<String, Object>> getUserChatDTOs(UUID userId, Pageable pageable) {
        // Get threads where user is either the fan or the artist
        Page<ChatThread> threads = threadRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);

        // If no threads found as user, check if user is an artist
        if (threads.isEmpty()) {
            Optional<ArtistProfile> artistProfile = artistProfileRepository.findByUserId(userId);
            if (artistProfile.isPresent()) {
                threads = threadRepository.findByArtistIdOrderByUpdatedAtDesc(artistProfile.get().getId(), pageable);
            }
        }

        List<Map<String, Object>> dtos = threads.getContent().stream().map(thread -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", thread.getId());
            dto.put("artistId", thread.getArtistId());
            dto.put("userId", thread.getUserId());
            dto.put("updatedAt", thread.getUpdatedAt());
            dto.put("createdAt", thread.getCreatedAt());

            // Get artist display name
            artistProfileRepository.findById(thread.getArtistId()).ifPresent(artist -> {
                dto.put("artistDisplayName", artist.getDisplayName());
                dto.put("artistAvatarUrl", artist.getAvatarUrl());
            });

            // Get user email
            userRepository.findById(thread.getUserId()).ifPresent(user -> {
                dto.put("userEmail", user.getEmail());
            });

            // Get last message
            var messages = messageRepository.findByThreadIdOrderByCreatedAtDesc(thread.getId(),
                    org.springframework.data.domain.PageRequest.of(0, 1));
            if (!messages.isEmpty()) {
                ChatMessage lastMsg = messages.getContent().get(0);
                dto.put("lastMessage", lastMsg.getMessage());
                dto.put("lastMessageAt", lastMsg.getCreatedAt());
            }

            // Get unread count
            long unreadCount = messageRepository.countByThreadIdAndReadAtIsNull(thread.getId());
            dto.put("unreadCount", unreadCount);

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, threads.getTotalElements());
    }

    public Page<ChatThread> getUserChats(UUID userId, Pageable pageable) {
        return threadRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);
    }

    public Page<ChatThread> getArtistChats(UUID artistId, Pageable pageable) {
        return threadRepository.findByArtistIdOrderByUpdatedAtDesc(artistId, pageable);
    }

    public void markMessagesAsRead(UUID threadId, UUID readerId) {
        ChatThread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Chat thread not found"));

        var unreadMessages = messageRepository.findByThreadIdAndReadAtIsNullAndSenderIdNot(threadId, readerId);
        unreadMessages.forEach(msg -> msg.setReadAt(LocalDateTime.now()));
        messageRepository.saveAll(unreadMessages);
    }

    /**
     * Called when an artist replies via WhatsApp.
     * Looks up the artist by phone number, finds the most recent chat thread,
     * saves the message with source=WHATSAPP, and notifies the fan.
     */
    public ChatMessageDTO saveWhatsAppReply(String phoneNumber, String messageText) {
        // Normalize phone: strip "whatsapp:" prefix if present
        String normalizedPhone = phoneNumber.replace("whatsapp:", "").trim();

        // Find the user account by phone number
        var userOpt = userRepository.findByPhoneNumber(normalizedPhone);
        if (userOpt.isEmpty()) {
            // Try with/without + prefix
            if (normalizedPhone.startsWith("+")) {
                userOpt = userRepository.findByPhoneNumber(normalizedPhone.substring(1));
            } else {
                userOpt = userRepository.findByPhoneNumber("+" + normalizedPhone);
            }
        }
        if (userOpt.isEmpty()) {
            log.warn("WhatsApp reply from unknown phone number: {}", normalizedPhone);
            throw new IllegalArgumentException("No user found with phone number: " + normalizedPhone);
        }

        var artistUser = userOpt.get();
        log.info("WhatsApp reply from user={} ({}), phone={}", artistUser.getId(), artistUser.getEmail(), normalizedPhone);

        // Find the artist profile for this user
        var artistProfile = artistProfileRepository.findByUserId(artistUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("No artist profile found for phone: " + normalizedPhone));

        // Find the most recently updated thread for this artist
        var thread = threadRepository.findTopByArtistIdOrderByUpdatedAtDesc(artistProfile.getId())
                .orElseThrow(() -> new IllegalArgumentException("No active chat thread found for this artist"));

        // Save the message with source=WHATSAPP
        ChatMessage msg = ChatMessage.builder()
                .id(UUID.randomUUID())
                .threadId(thread.getId())
                .senderId(artistUser.getId())
                .message(messageText)
                .source("WHATSAPP")
                .build();

        msg = messageRepository.save(msg);
        thread.setUpdatedAt(LocalDateTime.now());
        threadRepository.save(thread);

        // Notify the fan via in-app notification
        try {
            String artistName = artistProfile.getDisplayName() != null
                    ? artistProfile.getDisplayName() : artistUser.getEmail();
            notificationService.send(thread.getUserId(), "MESSAGE",
                    "Reply from " + artistName,
                    messageText.length() > 100 ? messageText.substring(0, 100) + "..." : messageText);
        } catch (Exception e) {
            log.warn("Failed to notify fan of WhatsApp reply: {}", e.getMessage());
        }

        log.info("WhatsApp reply saved in thread={} by artist={}", thread.getId(), artistUser.getId());
        return toChatMessageDTO(msg);
    }

    private ChatMessageDTO toChatMessageDTO(ChatMessage message) {
        String senderName = userRepository.findById(message.getSenderId())
                .map(user -> user.getEmail())
                .orElse("Unknown");

        return ChatMessageDTO.builder()
                .id(message.getId())
                .threadId(message.getThreadId())
                .senderId(message.getSenderId())
                .senderName(senderName)
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .readAt(message.getReadAt())
                .source(message.getSource())
                .build();
    }

}
