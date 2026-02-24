-- Additional composite indexes for 400K+ concurrent users
-- Optimizes the most frequent query patterns at scale

-- Booking: user + status composite (fan's booking list filtered by status)
CREATE INDEX IF NOT EXISTS idx_booking_user_status ON booking(user_id, status);

-- Booking: artist + status composite (artist's booking list filtered by status)
CREATE INDEX IF NOT EXISTS idx_booking_artist_status ON booking(artist_id, status);

-- Chat messages: thread + created_at desc (paginated message history)
CREATE INDEX IF NOT EXISTS idx_chat_message_thread_created ON chat_message(thread_id, created_at DESC);

-- Artist profile: featured partial index (homepage featured query)
CREATE INDEX IF NOT EXISTS idx_artist_featured_rating ON artist_profile(rating_aggregate DESC) WHERE featured = true;

-- Artist display name trigram index for fast LIKE search
-- (requires pg_trgm extension, skip if not available)
DO $$
BEGIN
    CREATE EXTENSION IF NOT EXISTS pg_trgm;
    CREATE INDEX IF NOT EXISTS idx_artist_displayname_trgm ON artist_profile USING gin (display_name gin_trgm_ops);
EXCEPTION WHEN OTHERS THEN
    RAISE NOTICE 'pg_trgm extension not available, skipping trigram index';
END $$;

