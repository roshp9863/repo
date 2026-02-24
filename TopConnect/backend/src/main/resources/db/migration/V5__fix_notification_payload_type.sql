-- Change payload_json from jsonb to text to avoid Hibernate type mismatch
ALTER TABLE notification ALTER COLUMN payload_json TYPE TEXT USING payload_json::TEXT;

