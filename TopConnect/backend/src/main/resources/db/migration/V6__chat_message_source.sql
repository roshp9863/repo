-- Add source column to chat_message for tracking WEB vs WHATSAPP messages
ALTER TABLE chat_message ADD COLUMN IF NOT EXISTS source VARCHAR(20) DEFAULT 'WEB';

