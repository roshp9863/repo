package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ArtistProfileDTO;
import org.connect.dto.ApiResponse;
import org.connect.dto.OfferingDTO;
import org.connect.security.SecurityUtils;
import org.connect.service.ArtistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Artists", description = "Artist profile and listing endpoints")
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping("/{artistId}")
    @Operation(summary = "Get artist profile by ID")
    public ResponseEntity<ApiResponse<ArtistProfileDTO>> getArtist(@PathVariable UUID artistId) {
        log.debug("Fetching artist profile: {}", artistId);
        ArtistProfileDTO artist = artistService.getArtistProfile(artistId);
        return ResponseEntity.ok(new ApiResponse<>("Artist retrieved", artist));
    }

    @GetMapping
    @Operation(summary = "List all artists with optional category filter")
    public ResponseEntity<ApiResponse<Page<ArtistProfileDTO>>> listArtists(
            @RequestParam(required = false) String category,
            Pageable pageable) {
        if (category != null && !category.isBlank()) {
            log.info("Listing artists by category={}", category);
            Page<ArtistProfileDTO> artists = artistService.listByCategory(category.toUpperCase(), pageable);
            return ResponseEntity.ok(new ApiResponse<>("Artists by category", artists));
        }
        log.debug("Listing artists, page={}", pageable.getPageNumber());
        Page<ArtistProfileDTO> artists = artistService.listArtists(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Artists retrieved", artists));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all available artist categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        List<String> categories = artistService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse<>("Categories retrieved", categories));
    }

    @GetMapping("/search")
    @Operation(summary = "Search artists by name")
    public ResponseEntity<ApiResponse<Page<ArtistProfileDTO>>> searchArtists(
            @RequestParam String query,
            Pageable pageable) {
        log.info("Searching artists with query='{}', page={}", query, pageable.getPageNumber());
        Page<ArtistProfileDTO> artists = artistService.searchArtists(query, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Search results", artists));
    }

    @GetMapping("/featured/list")
    @Operation(summary = "Get featured artists")
    public ResponseEntity<ApiResponse<Page<ArtistProfileDTO>>> getFeaturedArtists(Pageable pageable) {
        Page<ArtistProfileDTO> artists = artistService.getFeaturedArtists(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Featured artists", artists));
    }

    @GetMapping("/{artistId}/offerings")
    @Operation(summary = "Get artist offerings")
    public ResponseEntity<ApiResponse<Page<OfferingDTO>>> getArtistOfferings(
            @PathVariable UUID artistId,
            Pageable pageable) {
        log.debug("Fetching offerings for artist={}", artistId);
        Page<OfferingDTO> offerings = artistService.getArtistOfferings(artistId, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Offerings retrieved", offerings));
    }

    @GetMapping("/profile/me")
    @Operation(summary = "Get my artist profile")
    public ResponseEntity<ApiResponse<ArtistProfileDTO>> getMyProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        ArtistProfileDTO profile = artistService.getArtistProfileByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>("Profile retrieved", profile));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update artist profile (including categories)")
    public ResponseEntity<ApiResponse<ArtistProfileDTO>> updateProfile(@RequestBody ArtistProfileDTO dto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Updating artist profile for user={}", userId);
        ArtistProfileDTO updated = artistService.updateArtistProfile(userId, dto);
        return ResponseEntity.ok(new ApiResponse<>("Profile updated", updated));
    }
}
