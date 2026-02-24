package org.connect.repository;

import org.connect.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Page<Booking> findByUserIdOrderByScheduledAtDesc(UUID userId, Pageable pageable);

    Page<Booking> findByUserId(UUID userId, Pageable pageable);

    Page<Booking> findByArtistIdOrderByScheduledAtDesc(UUID artistId, Pageable pageable);

    Page<Booking> findByUserIdAndStatus(UUID userId, Booking.BookingStatus status, Pageable pageable);

    Page<Booking> findByArtistIdAndStatus(UUID artistId, Booking.BookingStatus status, Pageable pageable);

    List<Booking> findBySlotIdAndStatus(UUID slotId, Booking.BookingStatus status);

    Optional<Booking> findByIdAndUserId(UUID bookingId, UUID userId);

    Optional<Booking> findByIdAndArtistId(UUID bookingId, UUID artistId);

    List<Booking> findByScheduledAtBetweenAndStatus(LocalDateTime start, LocalDateTime end, Booking.BookingStatus status);

}

