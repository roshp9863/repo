package org.connect.dto;

import jakarta.validation.constraints.NotBlank;
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
public class PortfolioItemDTO {
    private UUID id;
    private UUID userId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    private String mediaUrl;
    private String mediaType;
    private String tags;
    private String visibility;
    private Integer sortOrder;
    private LocalDateTime createdAt;

    // Enriched fields for display
    private String userName;
    private String userRole;
}

