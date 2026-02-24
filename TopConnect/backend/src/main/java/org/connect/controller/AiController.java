package org.connect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ApiResponse;
import org.connect.security.SecurityUtils;
import org.connect.service.AiRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AI", description = "AI-powered features using Spring AI")
public class AiController {

    private final AiRecommendationService aiService;

    @GetMapping("/recommendations")
    @Operation(summary = "Get AI-powered artist recommendations for the current user")
    public ResponseEntity<ApiResponse<Map<String, String>>> getRecommendations() {
        UUID userId = SecurityUtils.getCurrentUserId();
        log.info("Generating AI recommendations for user={}", userId);
        String recommendations = aiService.getRecommendations(userId);
        return ResponseEntity.ok(new ApiResponse<>("Recommendations generated",
                Map.of("recommendations", recommendations)));
    }

    @PostMapping("/session-summary")
    @Operation(summary = "Generate AI session summary from booking notes")
    public ResponseEntity<ApiResponse<Map<String, String>>> generateSessionSummary(
            @RequestBody Map<String, String> request) {
        String goalNotes = request.get("goalNotes");
        String sessionNotes = request.get("sessionNotes");
        String offeringTitle = request.get("offeringTitle");
        log.info("Generating AI session summary for: {}", offeringTitle);
        String summary = aiService.generateSessionSummary(goalNotes, sessionNotes, offeringTitle);
        return ResponseEntity.ok(new ApiResponse<>("Summary generated",
                Map.of("summary", summary)));
    }

    @GetMapping("/smart-search")
    @Operation(summary = "AI-powered natural language artist search")
    public ResponseEntity<ApiResponse<Map<String, String>>> smartSearch(
            @RequestParam String query) {
        log.info("AI smart search: {}", query);
        String results = aiService.smartSearch(query);
        return ResponseEntity.ok(new ApiResponse<>("Search results",
                Map.of("results", results)));
    }
}

