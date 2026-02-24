package org.connect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private UUID id;

    @NotNull(message = "Offering ID is required")
    private UUID offeringId;
    private UUID artistId;
    private UUID userId;
    private UUID slotId;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private String status;
    private String notes;
    private String meetingLink;
    private String goalNotes;
    private LocalDateTime createdAt;

    // Enriched fields for frontend display
    private String offeringTitle;
    private String offeringDescription;
    private String offeringType;
    private BigDecimal offeringPrice;
    private String offeringCurrency;
    private String artistDisplayName;
    private String artistAvatarUrl;
}
