package org.connect.repository;

import org.connect.entity.CommissionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommissionConfigRepository extends JpaRepository<CommissionConfig, UUID> {

    Optional<CommissionConfig> findByArtistId(UUID artistId);

    /** Global default where artist_id IS NULL */
    Optional<CommissionConfig> findByArtistIdIsNull();
}

