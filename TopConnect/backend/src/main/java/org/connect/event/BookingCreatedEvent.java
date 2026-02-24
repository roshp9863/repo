package org.connect.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * Published when a booking is created.
 * Listeners can send notifications, emails, WhatsApp messages asynchronously
 * without blocking the booking transaction.
 */
@Getter
public class BookingCreatedEvent extends ApplicationEvent {

    private final UUID bookingId;
    private final UUID fanUserId;
    private final UUID artistUserId;
    private final String offeringTitle;

    public BookingCreatedEvent(Object source, UUID bookingId, UUID fanUserId,
                                UUID artistUserId, String offeringTitle) {
        super(source);
        this.bookingId = bookingId;
        this.fanUserId = fanUserId;
        this.artistUserId = artistUserId;
        this.offeringTitle = offeringTitle;
    }
}

