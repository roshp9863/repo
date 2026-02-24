package org.connect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistProfileDTO {
    private UUID id;
    private UUID userId;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private String timezone;
    private Boolean featured;
    private BigDecimal ratingAggregate;
    private Integer ratingCount;
    private LocalDateTime createdAt;
    private List<String> categories;
    private Boolean hasChatOffering;

    // Verification
    private String verificationStatus;
    private String instagramUrl;
    private String youtubeUrl;
    private String certificateInfo;
    private String rejectionReason;
}

