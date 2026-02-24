-- Add title and message columns to notification table
ALTER TABLE notification ADD COLUMN IF NOT EXISTS title VARCHAR(255);
ALTER TABLE notification ADD COLUMN IF NOT EXISTS message TEXT;

