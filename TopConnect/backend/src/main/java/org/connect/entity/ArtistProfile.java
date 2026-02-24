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
@Table(name = "artist_profile", indexes = {
    @Index(name = "idx_artist_profile_user_id", columnList = "user_id"),
    @Index(name = "idx_artist_profile_featured", columnList = "featured")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistProfile {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true, columnDefinition = "uuid")
    private UUID userId;

    @Column(nullable = false)
    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 500)
    private String avatarUrl;

    @Column(length = 50)
    private String timezone;

    @Column(nullable = false)
    private Boolean featured;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING_VERIFICATION;

    @Column(length = 500)
    private String instagramUrl;

    @Column(length = 500)
    private String youtubeUrl;

    @Column(columnDefinition = "TEXT")
    private String certificateInfo;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Column
    private LocalDateTime verifiedAt;

    @Column(columnDefinition = "uuid")
    private UUID verifiedBy;

    @Column(precision = 3, scale = 2)
    private BigDecimal ratingAggregate;

    @Column
    private Integer ratingCount;

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
        if (this.featured == null) {
            this.featured = false;
        }
        if (this.verificationStatus == null) {
            this.verificationStatus = VerificationStatus.PENDING_VERIFICATION;
        }
        if (this.ratingAggregate == null) {
            this.ratingAggregate = BigDecimal.ZERO;
        }
        if (this.ratingCount == null) {
            this.ratingCount = 0;
        }
    }

    public enum VerificationStatus {
        PENDING_VERIFICATION,  // just signed up, awaiting admin review
        VERIFIED,              // admin approved
        REJECTED               // admin rejected (reason stored in rejectionReason)
    }
}

