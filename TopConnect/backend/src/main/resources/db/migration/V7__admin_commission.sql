-- Commission configuration table
CREATE TABLE IF NOT EXISTS commission_config (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    artist_id UUID UNIQUE,
    commission_rate NUMERIC(5,2) NOT NULL DEFAULT 10.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

-- Default platform commission (artist_id = NULL means global default)
INSERT INTO commission_config (id, artist_id, commission_rate)
VALUES (uuid_generate_v4(), NULL, 10.00)
ON CONFLICT DO NOTHING;

CREATE INDEX idx_commission_config_artist_id ON commission_config(artist_id);

