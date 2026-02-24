-- Add phone number and WhatsApp opt-in fields
ALTER TABLE user_account ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);
ALTER TABLE user_account ADD COLUMN IF NOT EXISTS whatsapp_opt_in BOOLEAN NOT NULL DEFAULT false;

