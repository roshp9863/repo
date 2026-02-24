package org.connect.repository;

import org.connect.entity.ArtistProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, UUID> {

    Optional<ArtistProfile> findByUserId(UUID userId);

    Page<ArtistProfile> findAll(Pageable pageable);

    // ── Public queries: only VERIFIED artists ──────────────────────

    @Query("SELECT ap FROM ArtistProfile ap WHERE ap.verificationStatus = 'VERIFIED' AND LOWER(ap.displayName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ArtistProfile> searchByDisplayName(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT ap FROM ArtistProfile ap WHERE ap.verificationStatus = 'VERIFIED' AND ap.featured = true")
    Page<ArtistProfile> findByFeaturedTrue(Pageable pageable);

    @Query("SELECT ap FROM ArtistProfile ap WHERE ap.verificationStatus = 'VERIFIED' AND ap.id IN :ids")
    Page<ArtistProfile> findByIdIn(@Param("ids") List<UUID> ids, Pageable pageable);

    @Query("SELECT ap FROM ArtistProfile ap WHERE ap.verificationStatus = 'VERIFIED' ORDER BY ap.ratingAggregate DESC")
    Page<ArtistProfile> findTopRated(Pageable pageable);

    // ── Admin queries: by verification status ──────────────────────

    Page<ArtistProfile> findByVerificationStatus(ArtistProfile.VerificationStatus status, Pageable pageable);

    long countByVerificationStatus(ArtistProfile.VerificationStatus status);
}

