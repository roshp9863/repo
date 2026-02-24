package org.connect.repository;

import org.connect.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findByArtistIdOrderByCreatedAtDesc(UUID artistId, Pageable pageable);

    Page<Review> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Optional<Review> findByBookingId(UUID bookingId);

    Optional<Review> findByBookingIdAndUserId(UUID bookingId, UUID userId);

    boolean existsByBookingIdAndUserId(UUID bookingId, UUID userId);

    List<Review> findByArtistId(UUID artistId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.artistId = :artistId")
    Double findAverageRatingByArtistId(UUID artistId);

    long countByArtistId(UUID artistId);
}

