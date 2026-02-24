package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.NotificationDTO;
import org.connect.entity.Notification;
import org.connect.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Send an in-app notification to a user.
     */
    public void send(UUID userId, String type, String title, String message) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .channel(Notification.NotificationChannel.IN_APP)
                .status(Notification.NotificationStatus.PENDING)
                .sentAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        log.debug("Notification sent to user={}: {}", userId, title);
    }

    /**
     * Get paginated notifications for a user.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> getUserNotifications(UUID userId, Pageable pageable) {
        Page<Notification> page = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        List<NotificationDTO> dtos = page.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    /**
     * Count unread notifications.
     */
    @Transactional(readOnly = true)
    public long countUnread(UUID userId) {
        return notificationRepository.countByUserIdAndStatus(userId, Notification.NotificationStatus.PENDING);
    }

    /**
     * Mark a single notification as read.
     */
    public void markAsRead(UUID userId, UUID notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElse(null);
        if (n != null && n.getUserId().equals(userId)) {
            n.setStatus(Notification.NotificationStatus.SENT);
            notificationRepository.save(n);
        }
    }

    /**
     * Mark all notifications as read for a user.
     */
    public void markAllAsRead(UUID userId) {
        List<Notification> unread = notificationRepository
                .findByUserIdAndStatus(userId, Notification.NotificationStatus.PENDING);
        unread.forEach(n -> n.setStatus(Notification.NotificationStatus.SENT));
        notificationRepository.saveAll(unread);
        log.info("Marked {} notifications as read for user={}", unread.size(), userId);
    }

    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .type(n.getType())
                .title(n.getTitle())
                .message(n.getMessage())
                .channel(n.getChannel().name())
                .status(n.getStatus().name())
                .read(n.getStatus() == Notification.NotificationStatus.SENT)
                .createdAt(n.getCreatedAt())
                .build();
    }
}

