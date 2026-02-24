-- Add tags column to review for predefined comment tags
ALTER TABLE review ADD COLUMN IF NOT EXISTS tags TEXT;

-- Add unique constraint: one review per booking (PostgreSQL doesn't support IF NOT EXISTS for constraints)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uq_review_booking'
    ) THEN
        ALTER TABLE review ADD CONSTRAINT uq_review_booking UNIQUE (booking_id);
    END IF;
END
$$;

-- Add index for sorting artists by rating
CREATE INDEX IF NOT EXISTS idx_artist_profile_rating ON artist_profile(rating_aggregate DESC NULLS LAST);

