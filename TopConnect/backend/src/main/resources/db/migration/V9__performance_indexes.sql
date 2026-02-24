-- Additional composite indexes for performance optimization
-- Most single-column indexes were already created in V1.
-- These add composite indexes for common query patterns.

-- Notification: user + status composite (for unread count queries)
CREATE INDEX IF NOT EXISTS idx_notification_user_status ON notification(user_id, status);

-- Notification: user + created_at desc (for paginated notification list)
CREATE INDEX IF NOT EXISTS idx_notification_user_created ON notification(user_id, created_at DESC);

-- Offering: artist + active composite (for listing active offerings)
CREATE INDEX IF NOT EXISTS idx_offering_artist_active ON offering(artist_id, active);


