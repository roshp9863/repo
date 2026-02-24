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

/**
 * Portfolio item for both artists and fans.
 * Artists: showcase work samples, achievements, certifications.
 * Fans: track their learning journey, goals, progress.
 */
@Entity
@Table(name = "portfolio_item", indexes = {
    @Index(name = "idx_portfolio_user_id", columnList = "user_id"),
    @Index(name = "idx_portfolio_category", columnList = "category"),
    @Index(name = "idx_portfolio_sort", columnList = "sort_order")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioItem {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** Category: e.g. "WORK_SAMPLE", "ACHIEVEMENT", "CERTIFICATION", "GOAL", "JOURNEY", "SKILL" */
    @Column(nullable = false, length = 50)
    private String category;

    /** Optional media URL (image, video, audio, document link) */
    @Column(length = 1000)
    private String mediaUrl;

    /** Media type: IMAGE, VIDEO, AUDIO, LINK, DOCUMENT */
    @Column(length = 20)
    private String mediaType;

    /** Tags for searchability (comma-separated) */
    @Column(length = 500)
    private String tags;

    /** Visibility: PUBLIC (everyone), PRIVATE (only self) */
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String visibility = "PUBLIC";

    @Column
    @Builder.Default
    private Integer sortOrder = 0;

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

