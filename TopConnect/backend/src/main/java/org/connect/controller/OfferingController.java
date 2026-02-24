package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.OfferingDTO;
import org.connect.dto.ApiResponse;
import org.connect.security.SecurityUtils;
import org.connect.service.ArtistService;
import org.connect.service.OfferingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offerings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Offerings", description = "Offering management endpoints")
public class OfferingController {

    private final OfferingService offeringService;
    private final ArtistService artistService;

    private UUID getArtistProfileId() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return artistService.getArtistProfileByUserId(userId).getId();
    }

    @GetMapping("/mine")
    @Operation(summary = "List my offerings (artist)")
    public ResponseEntity<ApiResponse<List<OfferingDTO>>> getMyOfferings() {
        UUID artistId = getArtistProfileId();
        log.info("Fetching offerings for artist={}", artistId);
        List<OfferingDTO> offerings = offeringService.listMyOfferings(artistId);
        return ResponseEntity.ok(new ApiResponse<>("My offerings retrieved", offerings));
    }

    @PostMapping
    @Operation(summary = "Create a new offering (artist)")
    public ResponseEntity<ApiResponse<OfferingDTO>> createOffering(@Valid @RequestBody OfferingDTO request) {
        UUID artistId = getArtistProfileId();
        log.info("Creating offering for artist={}, title={}", artistId, request.getTitle());
        OfferingDTO offering = offeringService.createOffering(artistId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Offering created", offering));
    }

    @GetMapping("/{offeringId}")
    @Operation(summary = "Get offering details")
    public ResponseEntity<ApiResponse<OfferingDTO>> getOffering(@PathVariable UUID offeringId) {
        OfferingDTO offering = offeringService.getOffering(offeringId);
        return ResponseEntity.ok(new ApiResponse<>("Offering retrieved", offering));
    }

    @GetMapping
    @Operation(summary = "List all active offerings")
    public ResponseEntity<ApiResponse<Page<OfferingDTO>>> listOfferings(Pageable pageable) {
        Page<OfferingDTO> offerings = offeringService.listOfferings(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Offerings retrieved", offerings));
    }

    @PutMapping("/{offeringId}")
    @Operation(summary = "Update offering (artist)")
    public ResponseEntity<ApiResponse<OfferingDTO>> updateOffering(
            @PathVariable UUID offeringId,
            @RequestBody OfferingDTO request) {
        UUID artistId = getArtistProfileId();
        log.info("Updating offering={} by artist={}", offeringId, artistId);
        OfferingDTO offering = offeringService.updateOffering(offeringId, artistId, request);
        return ResponseEntity.ok(new ApiResponse<>("Offering updated", offering));
    }

    @DeleteMapping("/{offeringId}")
    @Operation(summary = "Delete offering (artist)")
    public ResponseEntity<ApiResponse<String>> deleteOffering(@PathVariable UUID offeringId) {
        UUID artistId = getArtistProfileId();
        log.info("Deleting offering={} by artist={}", offeringId, artistId);
        offeringService.deleteOffering(offeringId, artistId);
        return ResponseEntity.ok(new ApiResponse<>("Offering deleted", null));
    }
}
