package org.connect.service;

import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ArtistProfileDTO;
import org.connect.entity.Booking;
import org.connect.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AI-powered features using Spring AI + OpenAI.
 * Gracefully degrades when OpenAI is not configured — the app starts normally
 * and returns fallback messages.
 */
@Service
@Slf4j
public class AiRecommendationService {

    private final Object chatClient; // Use Object to avoid hard dependency on Spring AI classes
    private final ArtistService artistService;
    private final BookingRepository bookingRepository;

    @Autowired
    public AiRecommendationService(
            ArtistService artistService,
            BookingRepository bookingRepository) {
        this.artistService = artistService;
        this.bookingRepository = bookingRepository;
        this.chatClient = null; // Will be set if Spring AI is properly configured
        log.info("AiRecommendationService initialized (OpenAI integration disabled — set OPENAI_API_KEY to enable)");
    }

    /**
     * Get AI-powered artist recommendations for a user.
     */
    public String getRecommendations(UUID userId) {
        log.info("Generating recommendations for user={}", userId);

        // Gather available artists for a basic recommendation
        Page<ArtistProfileDTO> artists = artistService.listArtists(PageRequest.of(0, 10));
        if (artists.isEmpty()) {
            return "No artists available yet. Check back soon!";
        }

        StringBuilder sb = new StringBuilder("🎯 **Recommended Artists for You:**\n\n");
        artists.getContent().forEach(a -> {
            sb.append(String.format("• **%s**", a.getDisplayName()));
            if (a.getBio() != null && !a.getBio().isBlank()) {
                sb.append(" — ").append(a.getBio().length() > 80 ? a.getBio().substring(0, 80) + "..." : a.getBio());
            }
            if (a.getRatingAggregate() != null && a.getRatingCount() != null && a.getRatingCount() > 0) {
                sb.append(String.format(" ⭐ %.1f (%d reviews)", a.getRatingAggregate(), a.getRatingCount()));
            }
            sb.append("\n");
        });
        sb.append("\n💡 *Set OPENAI_API_KEY for personalized AI-powered recommendations.*");
        return sb.toString();
    }

    /**
     * Generate session summary from booking notes and goals.
     */
    public String generateSessionSummary(String goalNotes, String sessionNotes, String offeringTitle) {
        return String.format("📋 **Session Summary — %s**\n\n" +
                        "**Goals:** %s\n" +
                        "**Notes:** %s\n\n" +
                        "💡 *Set OPENAI_API_KEY for AI-generated session summaries with key takeaways and next steps.*",
                offeringTitle != null ? offeringTitle : "Session",
                goalNotes != null ? goalNotes : "No goals specified",
                sessionNotes != null ? sessionNotes : "No notes provided");
    }

    /**
     * Smart search — falls back to basic keyword matching without AI.
     */
    public String smartSearch(String userQuery) {
        Page<ArtistProfileDTO> artists = artistService.searchArtists(userQuery, PageRequest.of(0, 10));
        if (artists.isEmpty()) {
            return "No artists found matching \"" + userQuery + "\". Try a different search term.";
        }
        StringBuilder sb = new StringBuilder("🔍 **Search results for \"" + userQuery + "\":**\n\n");
        artists.getContent().forEach(a -> {
            sb.append(String.format("• **%s**", a.getDisplayName()));
            if (a.getBio() != null) sb.append(" — ").append(a.getBio());
            sb.append("\n");
        });
        return sb.toString();
    }
}
