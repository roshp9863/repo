package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.ArtistProfileDTO;
import org.connect.dto.OfferingDTO;
import org.connect.entity.ArtistCategory;
import org.connect.entity.ArtistProfile;
import org.connect.entity.Offering;
import org.connect.repository.ArtistCategoryRepository;
import org.connect.repository.ArtistProfileRepository;
import org.connect.repository.OfferingRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistProfileRepository artistRepository;
    private final OfferingRepository offeringRepository;
    private final ArtistCategoryRepository categoryRepository;

    @Cacheable(value = "artistProfile", key = "#artistId")
    public ArtistProfileDTO getArtistProfile(UUID artistId) {
        ArtistProfile artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        return toArtistProfileDTO(artist);
    }

    public ArtistProfileDTO getArtistProfileByUserId(UUID userId) {
        ArtistProfile artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));
        return toArtistProfileDTO(artist);
    }

    public Page<ArtistProfileDTO> listArtists(Pageable pageable) {
        Page<ArtistProfile> artists = artistRepository.findTopRated(pageable);
        List<ArtistProfileDTO> dtos = toArtistProfileDTOsBatch(artists.getContent());
        return new PageImpl<>(dtos, pageable, artists.getTotalElements());
    }

    public Page<ArtistProfileDTO> listByCategory(String category, Pageable pageable) {
        List<UUID> artistIds = categoryRepository.findArtistIdsByCategory(category);
        if (artistIds.isEmpty()) {
            return Page.empty(pageable);
        }
        Page<ArtistProfile> artists = artistRepository.findByIdIn(artistIds, pageable);
        List<ArtistProfileDTO> dtos = toArtistProfileDTOsBatch(artists.getContent());
        return new PageImpl<>(dtos, pageable, artists.getTotalElements());
    }

    @Cacheable(value = "categories")
    public List<String> getAllCategories() {
        return categoryRepository.findDistinctCategories();
    }

    public Page<ArtistProfileDTO> searchArtists(String searchTerm, Pageable pageable) {
        Page<ArtistProfile> artists = artistRepository.searchByDisplayName(searchTerm, pageable);
        List<ArtistProfileDTO> dtos = toArtistProfileDTOsBatch(artists.getContent());
        return new PageImpl<>(dtos, pageable, artists.getTotalElements());
    }

    public Page<ArtistProfileDTO> getFeaturedArtists(Pageable pageable) {
        Page<ArtistProfile> artists = artistRepository.findByFeaturedTrue(pageable);
        List<ArtistProfileDTO> dtos = toArtistProfileDTOsBatch(artists.getContent());
        return new PageImpl<>(dtos, pageable, artists.getTotalElements());
    }

    public Page<OfferingDTO> getArtistOfferings(UUID artistId, Pageable pageable) {
        Page<Offering> offerings = offeringRepository.findByArtistIdAndActiveTrue(artistId, pageable);
        return new PageImpl<>(
                offerings.getContent().stream().map(this::toOfferingDTO).collect(Collectors.toList()),
                pageable,
                offerings.getTotalElements()
        );
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "artistProfile", allEntries = true),
            @CacheEvict(value = "artists", allEntries = true),
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "featuredArtists", allEntries = true)
    })
    public ArtistProfileDTO updateArtistProfile(UUID userId, ArtistProfileDTO dto) {
        ArtistProfile artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));

        if (dto.getDisplayName() != null) artist.setDisplayName(dto.getDisplayName());
        if (dto.getBio() != null) artist.setBio(dto.getBio());
        if (dto.getAvatarUrl() != null) artist.setAvatarUrl(dto.getAvatarUrl());
        if (dto.getTimezone() != null) artist.setTimezone(dto.getTimezone());

        if (dto.getCategories() != null) {
            categoryRepository.deleteByArtistId(artist.getId());
            for (String cat : dto.getCategories()) {
                if (cat != null && !cat.isBlank()) {
                    categoryRepository.save(ArtistCategory.builder()
                            .artistId(artist.getId())
                            .category(cat.trim().toUpperCase())
                            .build());
                }
            }
        }

        artist = artistRepository.save(artist);
        return toArtistProfileDTO(artist);
    }

    // ─── Batch DTO mapping (eliminates N+1 queries) ─────────────
    // Before: listing 20 artists = 1 + 20*2 = 41 queries
    // After:  listing 20 artists = 1 + 2    = 3  queries

    private List<ArtistProfileDTO> toArtistProfileDTOsBatch(List<ArtistProfile> artists) {
        if (artists.isEmpty()) return Collections.emptyList();

        List<UUID> artistIds = artists.stream().map(ArtistProfile::getId).collect(Collectors.toList());

        // 1 query: load ALL categories for all artists on this page
        Map<UUID, List<String>> categoriesByArtist = categoryRepository.findByArtistIdIn(artistIds)
                .stream()
                .collect(Collectors.groupingBy(
                        ArtistCategory::getArtistId,
                        Collectors.mapping(ArtistCategory::getCategory, Collectors.toList())
                ));

        // 1 query: check which artists have CHAT offerings
        Set<UUID> chatArtistIds = new HashSet<>(offeringRepository.findArtistIdsWithChatOffering(artistIds));

        return artists.stream().map(artist -> ArtistProfileDTO.builder()
                .id(artist.getId())
                .userId(artist.getUserId())
                .displayName(artist.getDisplayName())
                .bio(artist.getBio())
                .avatarUrl(artist.getAvatarUrl())
                .timezone(artist.getTimezone())
                .featured(artist.getFeatured())
                .ratingAggregate(artist.getRatingAggregate())
                .ratingCount(artist.getRatingCount())
                .createdAt(artist.getCreatedAt())
                .categories(categoriesByArtist.getOrDefault(artist.getId(), Collections.emptyList()))
                .hasChatOffering(chatArtistIds.contains(artist.getId()))
                .verificationStatus(artist.getVerificationStatus() != null ? artist.getVerificationStatus().name() : null)
                .instagramUrl(artist.getInstagramUrl())
                .youtubeUrl(artist.getYoutubeUrl())
                .certificateInfo(artist.getCertificateInfo())
                .rejectionReason(artist.getRejectionReason())
                .build()
        ).collect(Collectors.toList());
    }

    // Single-entity DTO (used for getArtistProfile, updateProfile)
    private ArtistProfileDTO toArtistProfileDTO(ArtistProfile artist) {
        List<String> categories = categoryRepository.findByArtistId(artist.getId())
                .stream().map(ArtistCategory::getCategory).collect(Collectors.toList());

        boolean hasChatOffering = offeringRepository.findByArtistIdAndActiveTrue(artist.getId())
                .stream().anyMatch(o -> o.getType() == Offering.OfferingType.CHAT);

        return ArtistProfileDTO.builder()
                .id(artist.getId())
                .userId(artist.getUserId())
                .displayName(artist.getDisplayName())
                .bio(artist.getBio())
                .avatarUrl(artist.getAvatarUrl())
                .timezone(artist.getTimezone())
                .featured(artist.getFeatured())
                .ratingAggregate(artist.getRatingAggregate())
                .ratingCount(artist.getRatingCount())
                .createdAt(artist.getCreatedAt())
                .categories(categories)
                .hasChatOffering(hasChatOffering)
                .verificationStatus(artist.getVerificationStatus() != null ? artist.getVerificationStatus().name() : null)
                .instagramUrl(artist.getInstagramUrl())
                .youtubeUrl(artist.getYoutubeUrl())
                .certificateInfo(artist.getCertificateInfo())
                .rejectionReason(artist.getRejectionReason())
                .build();
    }

    private OfferingDTO toOfferingDTO(Offering offering) {
        return OfferingDTO.builder()
                .id(offering.getId())
                .artistId(offering.getArtistId())
                .type(offering.getType().name())
                .title(offering.getTitle())
                .description(offering.getDescription())
                .durationMinutes(offering.getDurationMinutes())
                .price(offering.getPrice())
                .currency(offering.getCurrency())
                .active(offering.getActive())
                .createdAt(offering.getCreatedAt())
                .build();
    }
}
