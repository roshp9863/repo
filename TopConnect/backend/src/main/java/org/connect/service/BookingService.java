package org.connect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.dto.BookingDTO;
import org.connect.entity.*;
import org.connect.event.BookingCreatedEvent;
import org.connect.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final OfferingRepository offeringRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final ChatThreadRepository chatThreadRepository;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final NotificationService notificationService;
    private final UserAccountRepository userAccountRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BookingDTO createBooking(UUID userId, UUID offeringId, BookingDTO dto) {
        // Only fans (USER role) can book services
        UserAccount currentUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (currentUser.getRole() == UserAccount.UserRole.ARTIST) {
            throw new IllegalArgumentException("Artists cannot book services. Please sign up with a fan account to book.");
        }

        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new IllegalArgumentException("Offering not found"));

        if (!offering.getActive()) {
            throw new IllegalArgumentException("Offering is not available");
        }


        UUID slotId = dto.getSlotId();
        LocalDateTime scheduledAt = dto.getScheduledAt();

        if (slotId != null) {
            // ── PESSIMISTIC LOCK: SELECT ... FOR UPDATE ──────────────────
            // This locks the slot row in the DB. If another fan tries to book
            // the same slot at the same time, they will WAIT here until this
            // transaction commits or rolls back.
            AvailabilitySlot slot = slotRepository.findAvailableSlotForUpdate(slotId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "This time slot is no longer available. It may have just been booked by another fan. Please choose a different slot."));

            // Mark slot as BOOKED so no one else can take it
            slot.setStatus(AvailabilitySlot.SlotStatus.BOOKED);
            slotRepository.save(slot);
            scheduledAt = slot.getStartTime();
            log.info("Slot {} locked and marked BOOKED for user {}", slotId, userId);
        }

        Booking booking;
        try {
            booking = Booking.builder()
                    .id(UUID.randomUUID())
                    .offeringId(offeringId)
                    .artistId(offering.getArtistId())
                    .userId(userId)
                    .slotId(slotId)
                    .scheduledAt(scheduledAt)
                    .durationMinutes(offering.getDurationMinutes())
                    .status(Booking.BookingStatus.PENDING)
                    .notes(dto.getNotes())
                    .goalNotes(dto.getGoalNotes())
                    .build();

            booking = bookingRepository.save(booking);
        } catch (DataIntegrityViolationException e) {
            // Unique constraint violation — another booking already exists for this slot/time
            log.warn("Double-booking prevented for slot={}, user={}: {}", slotId, userId, e.getMessage());
            throw new IllegalArgumentException(
                    "This slot was just booked by another fan. Please select a different time slot.");
        }

        // Create or get chat thread
        ChatThread thread = chatThreadRepository.findByArtistIdAndUserId(offering.getArtistId(), userId)
                .orElse(ChatThread.builder()
                        .id(UUID.randomUUID())
                        .artistId(offering.getArtistId())
                        .userId(userId)
                        .bookingId(booking.getId())
                        .build());
        chatThreadRepository.save(thread);

        // Publish event — notifications sent asynchronously (non-blocking)
        ArtistProfile artist = artistProfileRepository.findById(offering.getArtistId()).orElse(null);
        if (artist != null) {
            eventPublisher.publishEvent(new BookingCreatedEvent(
                    this, booking.getId(), userId, artist.getUserId(), offering.getTitle()));
        }

        log.info("Booking created: {}", booking.getId());
        return toBookingDTO(booking);
    }

    public BookingDTO getBooking(UUID bookingId, UUID userId) {
        // Try as fan first, then as artist
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId).orElse(null);
        if (booking == null) {
            // Check if user is the artist
            ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId).orElse(null);
            if (artistProfile != null) {
                booking = bookingRepository.findByIdAndArtistId(bookingId, artistProfile.getId()).orElse(null);
            }
        }
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found or access denied");
        }
        return toBookingDTO(booking);
    }

    public Page<BookingDTO> getUserBookings(UUID userId, Pageable pageable) {
        // Get bookings where the user is the FAN (made by them)
        List<Booking> allBookings = new ArrayList<>(
                bookingRepository.findByUserIdOrderByScheduledAtDesc(userId, pageable).getContent()
        );

        // If user is also an artist, merge bookings where they are the ARTIST (received by them)
        ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId).orElse(null);
        if (artistProfile != null) {
            List<Booking> artistBookings = bookingRepository
                    .findByArtistIdOrderByScheduledAtDesc(artistProfile.getId(), pageable).getContent();
            // Add artist bookings that aren't already in the list (avoid duplicates)
            for (Booking ab : artistBookings) {
                if (allBookings.stream().noneMatch(b -> b.getId().equals(ab.getId()))) {
                    allBookings.add(ab);
                }
            }
        }

        // Sort by scheduledAt descending
        allBookings.sort(Comparator.comparing(
                (Booking b) -> b.getScheduledAt() != null ? b.getScheduledAt() : b.getCreatedAt(),
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        List<BookingDTO> dtos = allBookings.stream().map(this::toBookingDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    public Page<BookingDTO> getArtistBookings(UUID artistId, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findByArtistIdOrderByScheduledAtDesc(artistId, pageable);
        return new PageImpl<>(
                bookings.getContent().stream().map(this::toBookingDTO).collect(Collectors.toList()),
                pageable,
                bookings.getTotalElements()
        );
    }

    public void confirmBooking(UUID bookingId, UUID userId) {
        // Resolve artist profile ID from user ID
        ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found. Only artists can confirm bookings."));
        Booking booking = bookingRepository.findByIdAndArtistId(bookingId, artistProfile.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or access denied"));

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new IllegalArgumentException("Only pending bookings can be confirmed");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // Mark slot as booked if it exists
        if (booking.getSlotId() != null) {
            AvailabilitySlot slot = slotRepository.findById(booking.getSlotId()).orElse(null);
            if (slot != null && slot.getStatus() == AvailabilitySlot.SlotStatus.AVAILABLE) {
                slot.setStatus(AvailabilitySlot.SlotStatus.BOOKED);
                slotRepository.save(slot);
            }
        }

        log.info("Booking confirmed: {}", bookingId);

        // Notify fan about confirmation
        try {
            Offering offering = offeringRepository.findById(booking.getOfferingId()).orElse(null);
            String title = offering != null ? offering.getTitle() : "Session";
            notificationService.send(booking.getUserId(), "BOOKING_CONFIRMED",
                    "Booking Confirmed!",
                    "Your booking for \"" + title + "\" has been confirmed by the artist.");
        } catch (Exception e) {
            log.warn("Notification failed for confirm {}: {}", bookingId, e.getMessage());
        }
    }

    public void cancelBooking(UUID bookingId, UUID userId, String reason) {
        // Try as fan first, then as artist
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId).orElse(null);
        if (booking == null) {
            ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId).orElse(null);
            if (artistProfile != null) {
                booking = bookingRepository.findByIdAndArtistId(bookingId, artistProfile.getId()).orElse(null);
            }
        }
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found or access denied");
        }

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Release slot if it was booked
        if (booking.getSlotId() != null) {
            AvailabilitySlot slot = slotRepository.findById(booking.getSlotId()).orElse(null);
            if (slot != null && slot.getStatus() == AvailabilitySlot.SlotStatus.BOOKED) {
                slot.setStatus(AvailabilitySlot.SlotStatus.AVAILABLE);
                slotRepository.save(slot);
            }
        }

        log.info("Booking cancelled: {}", bookingId);

        // WhatsApp notify both parties
        try {
            Offering offering = offeringRepository.findById(booking.getOfferingId()).orElse(null);
            String title = offering != null ? offering.getTitle() : "Session";
            whatsAppService.notifyBookingCancelled(booking.getUserId(), title, reason);
            ArtistProfile artist = artistProfileRepository.findById(booking.getArtistId()).orElse(null);
            if (artist != null) {
                whatsAppService.notifyBookingCancelled(artist.getUserId(), title, reason);
            }
        } catch (Exception e) {
            log.warn("WhatsApp notification failed for cancel {}: {}", bookingId, e.getMessage());
        }

        // In-app notifications for cancellation
        try {
            Offering offering = offeringRepository.findById(booking.getOfferingId()).orElse(null);
            String offeringTitle = offering != null ? offering.getTitle() : "Session";
            notificationService.send(booking.getUserId(), "BOOKING_CANCELLED",
                    "Booking Cancelled",
                    "Your booking for \"" + offeringTitle + "\" has been cancelled.");
            ArtistProfile artistP = artistProfileRepository.findById(booking.getArtistId()).orElse(null);
            if (artistP != null) {
                notificationService.send(artistP.getUserId(), "BOOKING_CANCELLED",
                        "Booking Cancelled",
                        "A booking for \"" + offeringTitle + "\" has been cancelled.");
            }
        } catch (Exception e) {
            log.warn("Notification failed for cancel {}: {}", bookingId, e.getMessage());
        }
    }

    public BookingDTO completeBooking(UUID bookingId, UUID userId) {
        // Resolve artist profile ID from user ID
        ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found. Only artists can complete bookings."));
        Booking booking = bookingRepository.findByIdAndArtistId(bookingId, artistProfile.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or access denied"));

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed bookings can be completed");
        }

        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking = bookingRepository.save(booking);

        log.info("Booking completed: {}", bookingId);

        // Notify fan about session completion
        try {
            Offering offering = offeringRepository.findById(booking.getOfferingId()).orElse(null);
            String offeringTitle = offering != null ? offering.getTitle() : "Session";
            notificationService.send(booking.getUserId(), "BOOKING_COMPLETED",
                    "Session Completed! 🎉",
                    "Your session \"" + offeringTitle + "\" has been marked as complete. Hope it was great!");
        } catch (Exception e) {
            log.warn("Notification failed for complete {}: {}", bookingId, e.getMessage());
        }

        return toBookingDTO(booking);
    }

    private BookingDTO toBookingDTO(Booking booking) {
        BookingDTO.BookingDTOBuilder builder = BookingDTO.builder()
                .id(booking.getId())
                .offeringId(booking.getOfferingId())
                .artistId(booking.getArtistId())
                .userId(booking.getUserId())
                .slotId(booking.getSlotId())
                .scheduledAt(booking.getScheduledAt())
                .durationMinutes(booking.getDurationMinutes())
                .status(booking.getStatus().name())
                .notes(booking.getNotes())
                .meetingLink(booking.getMeetingLink())
                .goalNotes(booking.getGoalNotes())
                .createdAt(booking.getCreatedAt());

        // Enrich with offering details
        try {
            offeringRepository.findById(booking.getOfferingId()).ifPresent(offering -> {
                builder.offeringTitle(offering.getTitle());
                builder.offeringDescription(offering.getDescription());
                builder.offeringType(offering.getType().name());
                builder.offeringPrice(offering.getPrice());
                builder.offeringCurrency(offering.getCurrency());
            });
        } catch (Exception e) {
            log.debug("Could not enrich offering info for booking: {}", booking.getId());
        }

        // Enrich with artist details
        try {
            artistProfileRepository.findById(booking.getArtistId()).ifPresent(artist -> {
                builder.artistDisplayName(artist.getDisplayName());
                builder.artistAvatarUrl(artist.getAvatarUrl());
            });
        } catch (Exception e) {
            log.debug("Could not enrich artist info for booking: {}", booking.getId());
        }

        return builder.build();
    }

}

