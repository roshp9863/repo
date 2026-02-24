-- Artist verification workflow
-- Artists must submit proof (social links, certificates) and be approved by admin

ALTER TABLE artist_profile ADD COLUMN IF NOT EXISTS verification_status VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION';
ALTER TABLE artist_profile ADD COLUMN IF NOT EXISTS instagram_url VARCHAR(500);
ALTER TABLE artist_profile ADD COLUMN IF NOT EXISTS youtube_url VARCHAR(500);
ALTER TABLE artist_profile ADD COLUMN IF NOT EXISTS certificate_info TEXT;
ALTER TABLE artist_profile ADD COLUMN IF NOT EXISTS rejection_reason TEXT;
ALTER TABLE artist_profile ADD COLUMN IF NOT EXISTS verified_at TIMESTAMP;
ALTER TABLE artist_profile ADD COLUMN IF NOT EXISTS verified_by UUID;

CREATE INDEX IF NOT EXISTS idx_artist_verification_status ON artist_profile(verification_status);

-- Update existing artists to VERIFIED so they keep working
UPDATE artist_profile SET verification_status = 'VERIFIED' WHERE verification_status = 'PENDING_VERIFICATION';

