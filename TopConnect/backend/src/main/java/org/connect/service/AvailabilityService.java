package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.entity.AvailabilitySlot;
import org.connect.repository.AvailabilitySlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AvailabilityService {

    private final AvailabilitySlotRepository slotRepository;

    public AvailabilitySlot createSlot(UUID artistId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create slots in the past");
        }

        AvailabilitySlot slot = AvailabilitySlot.builder()
                .id(UUID.randomUUID())
                .artistId(artistId)
                .startTime(startTime)
                .endTime(endTime)
                .status(AvailabilitySlot.SlotStatus.AVAILABLE)
                .build();

        slot = slotRepository.save(slot);
        log.info("Availability slot created: {} for artist: {}", slot.getId(), artistId);
        return slot;
    }

    public List<AvailabilitySlot> getAvailableSlots(UUID artistId, LocalDateTime startDate, LocalDateTime endDate) {
        return slotRepository.findAvailableSlots(artistId, startDate, endDate);
    }

    public void blockSlot(UUID slotId, UUID artistId) {
        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (!slot.getArtistId().equals(artistId)) {
            throw new IllegalArgumentException("Not authorized to block this slot");
        }

        slot.setStatus(AvailabilitySlot.SlotStatus.BLOCKED);
        slotRepository.save(slot);
        log.info("Slot blocked: {}", slotId);
    }

    public void deleteSlot(UUID slotId, UUID artistId) {
        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (!slot.getArtistId().equals(artistId)) {
            throw new IllegalArgumentException("Not authorized to delete this slot");
        }

        slotRepository.delete(slot);
        log.info("Slot deleted: {}", slotId);
    }

}

