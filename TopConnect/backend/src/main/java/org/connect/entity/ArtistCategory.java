package org.connect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "artist_category", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"artist_id", "category"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistCategory {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "artist_id", nullable = false, columnDefinition = "uuid")
    private UUID artistId;

    @Column(nullable = false, length = 100)
    private String category;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID();
    }
}

