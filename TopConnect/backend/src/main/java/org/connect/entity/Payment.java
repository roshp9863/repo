package org.connect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment", indexes = {
    @Index(name = "idx_payment_intent_id", columnList = "intent_id"),
    @Index(name = "idx_payment_booking_id", columnList = "booking_id"),
    @Index(name = "idx_payment_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true, columnDefinition = "uuid")
    private UUID bookingId;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false, unique = true)
    private String intentId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = PaymentStatus.PENDING;
        }
        if (this.provider == null) {
            this.provider = "STRIPE";
        }
        if (this.currency == null) {
            this.currency = "USD";
        }
    }

    public enum PaymentStatus {
        PENDING, SUCCEEDED, FAILED, CANCELLED
    }

}

