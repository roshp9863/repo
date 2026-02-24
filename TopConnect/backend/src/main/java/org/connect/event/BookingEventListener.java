package org.connect.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.connect.service.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Async event listener for booking events.
 * Runs notifications/emails/WhatsApp in a separate thread pool
 * so the booking transaction completes fast.
 *
 * At 400K scale, these fire-and-forget operations don't block the HTTP thread.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {

    private final NotificationService notificationService;

    @Async("taskExecutor")
    @EventListener
    public void handleBookingCreated(BookingCreatedEvent event) {
        try {
            // Notify artist
            notificationService.send(event.getArtistUserId(), "BOOKING_REQUEST",
                    "New Booking Request",
                    "You have a new booking request for \"" + event.getOfferingTitle() + "\"");

            // Notify fan
            notificationService.send(event.getFanUserId(), "BOOKING_CREATED",
                    "Booking Placed",
                    "Your booking for \"" + event.getOfferingTitle() + "\" has been placed. Awaiting payment.");

            log.info("Booking notifications sent async for booking={}", event.getBookingId());
        } catch (Exception e) {
            log.warn("Async notification failed for booking {}: {}", event.getBookingId(), e.getMessage());
        }
    }
}

