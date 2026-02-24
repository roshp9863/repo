package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ApiResponse;
import org.connect.dto.PortfolioItemDTO;
import org.connect.security.SecurityUtils;
import org.connect.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Portfolio", description = "Portfolio management for artists and fans")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get public portfolio of a user (artist or fan)")
    public ResponseEntity<ApiResponse<List<PortfolioItemDTO>>> getPublicPortfolio(@PathVariable UUID userId) {
        log.debug("Fetching public portfolio for user={}", userId);
        List<PortfolioItemDTO> items = portfolioService.getPublicPortfolio(userId);
        return ResponseEntity.ok(new ApiResponse<>("Portfolio retrieved", items));
    }

    @GetMapping("/mine")
    @Operation(summary = "Get my portfolio (all items including private)")
    public ResponseEntity<ApiResponse<List<PortfolioItemDTO>>> getMyPortfolio() {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.debug("Fetching own portfolio for user={}", userId);
        List<PortfolioItemDTO> items = portfolioService.getMyPortfolio(userId);
        return ResponseEntity.ok(new ApiResponse<>("My portfolio retrieved", items));
    }

    @GetMapping("/user/{userId}/category/{category}")
    @Operation(summary = "Get portfolio items by category")
    public ResponseEntity<ApiResponse<List<PortfolioItemDTO>>> getByCategory(
            @PathVariable UUID userId,
            @PathVariable String category) {
        List<PortfolioItemDTO> items = portfolioService.getPortfolioByCategory(userId, category.toUpperCase());
        return ResponseEntity.ok(new ApiResponse<>("Portfolio items retrieved", items));
    }

    @PostMapping
    @Operation(summary = "Add a portfolio item")
    public ResponseEntity<ApiResponse<PortfolioItemDTO>> createItem(@Valid @RequestBody PortfolioItemDTO dto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Creating portfolio item for user={}, title={}", userId, dto.getTitle());
        PortfolioItemDTO item = portfolioService.createItem(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Portfolio item created", item));
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "Update a portfolio item")
    public ResponseEntity<ApiResponse<PortfolioItemDTO>> updateItem(
            @PathVariable UUID itemId,
            @RequestBody PortfolioItemDTO dto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Updating portfolio item={} by user={}", itemId, userId);
        PortfolioItemDTO item = portfolioService.updateItem(userId, itemId, dto);
        return ResponseEntity.ok(new ApiResponse<>("Portfolio item updated", item));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete a portfolio item")
    public ResponseEntity<ApiResponse<String>> deleteItem(@PathVariable UUID itemId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Deleting portfolio item={} by user={}", itemId, userId);
        portfolioService.deleteItem(userId, itemId);
        return ResponseEntity.ok(new ApiResponse<>("Portfolio item deleted", null));
    }
}

