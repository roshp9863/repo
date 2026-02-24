package org.connect.repository;

import org.connect.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    List<Notification> findByUserIdAndStatus(UUID userId, Notification.NotificationStatus status);

    List<Notification> findByStatusAndChannel(Notification.NotificationStatus status, Notification.NotificationChannel channel);

    long countByUserIdAndStatus(UUID userId, Notification.NotificationStatus status);
}

