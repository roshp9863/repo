CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- User Account Table
CREATE TABLE user_account (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'ARTIST', 'USER')),
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'ACTIVE', 'SUSPENDED', 'DELETED')) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_user_account_email ON user_account(email);
CREATE INDEX idx_user_account_status ON user_account(status);

-- Artist Profile Table
CREATE TABLE artist_profile (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL UNIQUE,
    display_name VARCHAR(255) NOT NULL,
    bio TEXT,
    avatar_url VARCHAR(500),
    timezone VARCHAR(50) DEFAULT 'UTC',
    featured BOOLEAN DEFAULT FALSE,
    rating_aggregate NUMERIC(3,2) DEFAULT 0.00,
    rating_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_artist_profile_user_id ON artist_profile(user_id);
CREATE INDEX idx_artist_profile_featured ON artist_profile(featured);

-- Artist Categories (JSON array stored as TEXT for MVP)
CREATE TABLE artist_category (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    artist_id UUID NOT NULL,
    category VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id) ON DELETE CASCADE,
    UNIQUE(artist_id, category)
);

CREATE INDEX idx_artist_category_artist_id ON artist_category(artist_id);

-- Social Links
CREATE TABLE artist_social (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    artist_id UUID NOT NULL,
    platform VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

-- Offering Table
CREATE TABLE offering (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    artist_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('CALL', 'CHAT', 'VIDEO', 'DIGITAL_DOWNLOAD')),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INTEGER,
    price NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

CREATE INDEX idx_offering_artist_id ON offering(artist_id);
CREATE INDEX idx_offering_active ON offering(active);

-- Availability Slot Table
CREATE TABLE availability_slot (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    artist_id UUID NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('AVAILABLE', 'BOOKED', 'BLOCKED')) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id) ON DELETE CASCADE
);

CREATE INDEX idx_availability_slot_artist_id ON availability_slot(artist_id);
CREATE INDEX idx_availability_slot_start_time ON availability_slot(start_time);
CREATE INDEX idx_availability_slot_status ON availability_slot(status);

-- Booking Table
CREATE TABLE booking (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    offering_id UUID NOT NULL,
    artist_id UUID NOT NULL,
    user_id UUID NOT NULL,
    slot_id UUID,
    scheduled_at TIMESTAMP,
    duration_minutes INTEGER,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')) DEFAULT 'PENDING',
    notes TEXT,
    meeting_link VARCHAR(500),
    goal_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cancelled_at TIMESTAMP,
    FOREIGN KEY (offering_id) REFERENCES offering(id),
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id),
    FOREIGN KEY (user_id) REFERENCES user_account(id),
    FOREIGN KEY (slot_id) REFERENCES availability_slot(id)
);

CREATE INDEX idx_booking_user_id ON booking(user_id);
CREATE INDEX idx_booking_artist_id ON booking(artist_id);
CREATE INDEX idx_booking_offering_id ON booking(offering_id);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_booking_scheduled_at ON booking(scheduled_at);

-- Payment Table
CREATE TABLE payment (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id UUID NOT NULL UNIQUE,
    provider VARCHAR(50) NOT NULL DEFAULT 'STRIPE',
    intent_id VARCHAR(255) NOT NULL UNIQUE,
    amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'SUCCEEDED', 'FAILED', 'CANCELLED')) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB,
    FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
);

CREATE INDEX idx_payment_intent_id ON payment(intent_id);
CREATE INDEX idx_payment_booking_id ON payment(booking_id);
CREATE INDEX idx_payment_status ON payment(status);

-- Chat Thread Table
CREATE TABLE chat_thread (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    artist_id UUID NOT NULL,
    user_id UUID NOT NULL,
    booking_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id),
    FOREIGN KEY (user_id) REFERENCES user_account(id),
    FOREIGN KEY (booking_id) REFERENCES booking(id),
    UNIQUE(artist_id, user_id)
);

CREATE INDEX idx_chat_thread_artist_id ON chat_thread(artist_id);
CREATE INDEX idx_chat_thread_user_id ON chat_thread(user_id);
CREATE INDEX idx_chat_thread_booking_id ON chat_thread(booking_id);

-- Chat Message Table
CREATE TABLE chat_message (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    thread_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP,
    FOREIGN KEY (thread_id) REFERENCES chat_thread(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user_account(id)
);

CREATE INDEX idx_chat_message_thread_id ON chat_message(thread_id);
CREATE INDEX idx_chat_message_sender_id ON chat_message(sender_id);
CREATE INDEX idx_chat_message_created_at ON chat_message(created_at);

-- Review Table
CREATE TABLE review (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id UUID NOT NULL,
    user_id UUID NOT NULL,
    artist_id UUID NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(id),
    FOREIGN KEY (user_id) REFERENCES user_account(id),
    FOREIGN KEY (artist_id) REFERENCES artist_profile(id)
);

CREATE INDEX idx_review_artist_id ON review(artist_id);
CREATE INDEX idx_review_user_id ON review(user_id);

-- Notification Table
CREATE TABLE notification (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    type VARCHAR(100) NOT NULL,
    payload_json JSONB,
    channel VARCHAR(50) NOT NULL CHECK (channel IN ('EMAIL', 'IN_APP')) DEFAULT 'IN_APP',
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'SENT', 'FAILED')) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_notification_user_id ON notification(user_id);
CREATE INDEX idx_notification_status ON notification(status);

-- Email Verification Token Table
CREATE TABLE email_verification_token (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL UNIQUE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    verified_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_email_verification_token_user_id ON email_verification_token(user_id);
CREATE INDEX idx_email_verification_token_token ON email_verification_token(token);

-- Password Reset Token Table
CREATE TABLE password_reset_token (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_token_token ON password_reset_token(token);
CREATE INDEX idx_password_reset_token_user_id ON password_reset_token(user_id);

-- Refresh Token Table
CREATE TABLE refresh_token (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_user_id ON refresh_token(user_id);
CREATE INDEX idx_refresh_token_token_hash ON refresh_token(token_hash);

