-- Prevent double-booking: only ONE active (PENDING/CONFIRMED) booking per slot
-- If two fans try to book the same slot simultaneously, the DB will reject the second one.

-- Step 1: Unique partial index — only one non-cancelled booking per slot
CREATE UNIQUE INDEX IF NOT EXISTS uq_booking_slot_active
    ON booking(slot_id)
    WHERE slot_id IS NOT NULL AND status IN ('PENDING', 'CONFIRMED');

-- Step 2: Also prevent duplicate bookings for same user + offering + time
CREATE UNIQUE INDEX IF NOT EXISTS uq_booking_user_offering_time
    ON booking(user_id, offering_id, scheduled_at)
    WHERE status IN ('PENDING', 'CONFIRMED');

