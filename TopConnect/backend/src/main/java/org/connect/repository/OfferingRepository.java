package org.connect.repository;

import org.connect.entity.Offering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, UUID> {

    List<Offering> findByArtistIdAndActiveTrue(UUID artistId);

    Page<Offering> findByArtistIdAndActiveTrue(UUID artistId, Pageable pageable);

    List<Offering> findByArtistIdOrderByCreatedAtDesc(UUID artistId);

    Page<Offering> findByActiveTrue(Pageable pageable);

    Page<Offering> findByType(Offering.OfferingType type, Pageable pageable);

    /** Batch: find all artist IDs that have an active CHAT offering (avoids N+1) */
    @Query("SELECT DISTINCT o.artistId FROM Offering o WHERE o.active = true AND o.type = 'CHAT' AND o.artistId IN :artistIds")
    List<UUID> findArtistIdsWithChatOffering(@Param("artistIds") List<UUID> artistIds);
}

