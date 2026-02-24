package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.PortfolioItemDTO;
import org.connect.entity.PortfolioItem;
import org.connect.entity.UserAccount;
import org.connect.exception.ResourceNotFoundException;
import org.connect.repository.PortfolioItemRepository;
import org.connect.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortfolioService {

    private final PortfolioItemRepository portfolioRepository;
    private final UserAccountRepository userAccountRepository;

    /**
     * Get all portfolio items for a user (public view — only PUBLIC items).
     */
    @Transactional(readOnly = true)
    public List<PortfolioItemDTO> getPublicPortfolio(UUID userId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<PortfolioItem> items = portfolioRepository.findByUserIdAndVisibilityOrderBySortOrderAscCreatedAtDesc(userId, "PUBLIC");
        return items.stream().map(i -> toDTO(i, user)).collect(Collectors.toList());
    }

    /**
     * Get all portfolio items for the current user (own view — all items including PRIVATE).
     */
    @Transactional(readOnly = true)
    public List<PortfolioItemDTO> getMyPortfolio(UUID userId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<PortfolioItem> items = portfolioRepository.findByUserIdOrderBySortOrderAscCreatedAtDesc(userId);
        return items.stream().map(i -> toDTO(i, user)).collect(Collectors.toList());
    }

    /**
     * Get portfolio items by category.
     */
    @Transactional(readOnly = true)
    public List<PortfolioItemDTO> getPortfolioByCategory(UUID userId, String category) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<PortfolioItem> items = portfolioRepository.findByUserIdAndCategoryOrderBySortOrderAscCreatedAtDesc(userId, category);
        return items.stream().map(i -> toDTO(i, user)).collect(Collectors.toList());
    }

    /**
     * Create a new portfolio item.
     */
    public PortfolioItemDTO createItem(UUID userId, PortfolioItemDTO dto) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        PortfolioItem item = PortfolioItem.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory().toUpperCase())
                .mediaUrl(dto.getMediaUrl())
                .mediaType(dto.getMediaType())
                .tags(dto.getTags())
                .visibility(dto.getVisibility() != null ? dto.getVisibility() : "PUBLIC")
                .sortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0)
                .build();

        item = portfolioRepository.save(item);
        log.info("Portfolio item created: {} for user={}", item.getId(), userId);
        return toDTO(item, user);
    }

    /**
     * Update a portfolio item (only owner can update).
     */
    public PortfolioItemDTO updateItem(UUID userId, UUID itemId, PortfolioItemDTO dto) {
        PortfolioItem item = portfolioRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio item", "id", itemId));

        if (!item.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to update this portfolio item");
        }

        if (dto.getTitle() != null) item.setTitle(dto.getTitle());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getCategory() != null) item.setCategory(dto.getCategory().toUpperCase());
        if (dto.getMediaUrl() != null) item.setMediaUrl(dto.getMediaUrl());
        if (dto.getMediaType() != null) item.setMediaType(dto.getMediaType());
        if (dto.getTags() != null) item.setTags(dto.getTags());
        if (dto.getVisibility() != null) item.setVisibility(dto.getVisibility());
        if (dto.getSortOrder() != null) item.setSortOrder(dto.getSortOrder());

        item = portfolioRepository.save(item);
        log.info("Portfolio item updated: {}", itemId);

        UserAccount user = userAccountRepository.findById(userId).orElse(null);
        return toDTO(item, user);
    }

    /**
     * Delete a portfolio item (only owner can delete).
     */
    public void deleteItem(UUID userId, UUID itemId) {
        PortfolioItem item = portfolioRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio item", "id", itemId));

        if (!item.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to delete this portfolio item");
        }

        portfolioRepository.delete(item);
        log.info("Portfolio item deleted: {}", itemId);
    }

    private PortfolioItemDTO toDTO(PortfolioItem item, UserAccount user) {
        return PortfolioItemDTO.builder()
                .id(item.getId())
                .userId(item.getUserId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .mediaUrl(item.getMediaUrl())
                .mediaType(item.getMediaType())
                .tags(item.getTags())
                .visibility(item.getVisibility())
                .sortOrder(item.getSortOrder())
                .createdAt(item.getCreatedAt())
                .userName(user != null ? user.getEmail() : null)
                .userRole(user != null ? user.getRole().name() : null)
                .build();
    }
}

