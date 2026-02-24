package org.connect.repository;

import org.connect.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, UUID> {

    List<AvailabilitySlot> findByArtistIdAndStatus(UUID artistId, AvailabilitySlot.SlotStatus status);

    @Query("SELECT s FROM AvailabilitySlot s WHERE s.artistId = :artistId AND s.status = 'AVAILABLE' " +
           "AND s.startTime >= :startDate AND s.endTime <= :endDate ORDER BY s.startTime ASC")
    List<AvailabilitySlot> findAvailableSlots(@Param("artistId") UUID artistId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    Optional<AvailabilitySlot> findByIdAndStatus(UUID id, AvailabilitySlot.SlotStatus status);

    /**
     * Pessimistic write lock: SELECT ... FOR UPDATE
     * When Fan A calls this, the DB row is LOCKED.
     * Fan B calling this at the same time will WAIT until Fan A's transaction commits.
     * This prevents double-booking the same slot.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AvailabilitySlot s WHERE s.id = :id AND s.status = 'AVAILABLE'")
    Optional<AvailabilitySlot> findAvailableSlotForUpdate(@Param("id") UUID id);

}

