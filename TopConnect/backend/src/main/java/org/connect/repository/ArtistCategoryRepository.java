package org.connect.repository;

import org.connect.entity.ArtistCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtistCategoryRepository extends JpaRepository<ArtistCategory, UUID> {

    List<ArtistCategory> findByArtistId(UUID artistId);

    /** Batch-load categories for multiple artists (avoids N+1) */
    @Query("SELECT ac FROM ArtistCategory ac WHERE ac.artistId IN :artistIds")
    List<ArtistCategory> findByArtistIdIn(@Param("artistIds") List<UUID> artistIds);

    void deleteByArtistId(UUID artistId);

    @Query("SELECT DISTINCT ac.category FROM ArtistCategory ac ORDER BY ac.category")
    List<String> findDistinctCategories();

    @Query("SELECT ac.artistId FROM ArtistCategory ac WHERE ac.category = :category")
    List<UUID> findArtistIdsByCategory(String category);
}

