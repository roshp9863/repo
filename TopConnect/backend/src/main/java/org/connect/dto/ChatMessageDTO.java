package org.connect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private UUID id;
    private UUID threadId;
    private UUID senderId;
    private String senderName;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private String source;
}

