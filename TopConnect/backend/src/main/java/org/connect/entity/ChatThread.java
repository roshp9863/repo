package org.connect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_thread", indexes = {
    @Index(name = "idx_chat_thread_artist_id", columnList = "artist_id"),
    @Index(name = "idx_chat_thread_user_id", columnList = "user_id"),
    @Index(name = "idx_chat_thread_booking_id", columnList = "booking_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatThread {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID artistId;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(columnDefinition = "uuid")
    private UUID bookingId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

}

