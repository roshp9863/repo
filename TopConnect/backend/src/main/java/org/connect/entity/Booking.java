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
@Table(name = "booking", indexes = {
    @Index(name = "idx_booking_user_id", columnList = "user_id"),
    @Index(name = "idx_booking_artist_id", columnList = "artist_id"),
    @Index(name = "idx_booking_offering_id", columnList = "offering_id"),
    @Index(name = "idx_booking_status", columnList = "status"),
    @Index(name = "idx_booking_scheduled_at", columnList = "scheduled_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID offeringId;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID artistId;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(columnDefinition = "uuid")
    private UUID slotId;

    @Column
    private LocalDateTime scheduledAt;

    @Column
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 500)
    private String meetingLink;

    @Column(columnDefinition = "TEXT")
    private String goalNotes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime cancelledAt;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
    }

    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

}

