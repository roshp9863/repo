package org.connect.repository;

import org.connect.entity.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, UUID> {

    List<PortfolioItem> findByUserIdOrderBySortOrderAscCreatedAtDesc(UUID userId);

    List<PortfolioItem> findByUserIdAndVisibilityOrderBySortOrderAscCreatedAtDesc(UUID userId, String visibility);

    List<PortfolioItem> findByUserIdAndCategoryOrderBySortOrderAscCreatedAtDesc(UUID userId, String category);

    long countByUserId(UUID userId);
}

