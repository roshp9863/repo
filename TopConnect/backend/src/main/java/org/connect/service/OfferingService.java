package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.OfferingDTO;
import org.connect.entity.Offering;
import org.connect.repository.OfferingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OfferingService {

    private final OfferingRepository offeringRepository;

    public List<OfferingDTO> listMyOfferings(UUID artistId) {
        return offeringRepository.findByArtistIdOrderByCreatedAtDesc(artistId).stream()
                .map(this::toOfferingDTO)
                .collect(Collectors.toList());
    }

    public OfferingDTO createOffering(UUID artistId, OfferingDTO dto) {
        Offering offering = Offering.builder()
                .id(UUID.randomUUID())
                .artistId(artistId)
                .type(Offering.OfferingType.valueOf(dto.getType().toUpperCase()))
                .title(dto.getTitle())
                .description(dto.getDescription())
                .durationMinutes(dto.getDurationMinutes())
                .price(dto.getPrice())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "USD")
                .active(true)
                .build();

        offering = offeringRepository.save(offering);
        return toOfferingDTO(offering);
    }

    public OfferingDTO getOffering(UUID offeringId) {
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        return toOfferingDTO(offering);
    }

    public Page<OfferingDTO> listOfferings(Pageable pageable) {
        Page<Offering> offerings = offeringRepository.findByActiveTrue(pageable);
        return new PageImpl<>(
                offerings.getContent().stream().map(this::toOfferingDTO).collect(Collectors.toList()),
                pageable,
                offerings.getTotalElements()
        );
    }

    public Page<OfferingDTO> listOfferingsByType(Offering.OfferingType type, Pageable pageable) {
        Page<Offering> offerings = offeringRepository.findByType(type, pageable);
        return new PageImpl<>(
                offerings.getContent().stream().map(this::toOfferingDTO).collect(Collectors.toList()),
                pageable,
                offerings.getTotalElements()
        );
    }

    public OfferingDTO updateOffering(UUID offeringId, UUID artistId, OfferingDTO dto) {
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));

        if (!offering.getArtistId().equals(artistId)) {
            throw new IllegalArgumentException("Not authorized to update this offering");
        }

        if (dto.getType() != null) {
            try {
                offering.setType(Offering.OfferingType.valueOf(dto.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid offering type: {}", dto.getType());
            }
        }
        if (dto.getTitle() != null) {
            offering.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            offering.setDescription(dto.getDescription());
        }
        if (dto.getDurationMinutes() != null) {
            offering.setDurationMinutes(dto.getDurationMinutes());
        }
        if (dto.getPrice() != null) {
            offering.setPrice(dto.getPrice());
        }
        if (dto.getActive() != null) {
            offering.setActive(dto.getActive());
        }
        if (dto.getCurrency() != null) {
            offering.setCurrency(dto.getCurrency());
        }

        offering = offeringRepository.save(offering);
        return toOfferingDTO(offering);
    }

    public void deleteOffering(UUID offeringId, UUID artistId) {
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));

        if (!offering.getArtistId().equals(artistId)) {
            throw new IllegalArgumentException("Not authorized to delete this offering");
        }

        offering.setActive(false);
        offeringRepository.save(offering);
    }

    private OfferingDTO toOfferingDTO(Offering offering) {
        return OfferingDTO.builder()
                .id(offering.getId())
                .artistId(offering.getArtistId())
                .type(offering.getType().name())
                .title(offering.getTitle())
                .description(offering.getDescription())
                .durationMinutes(offering.getDurationMinutes())
                .price(offering.getPrice())
                .currency(offering.getCurrency())
                .active(offering.getActive())
                .createdAt(offering.getCreatedAt())
                .build();
    }

}
