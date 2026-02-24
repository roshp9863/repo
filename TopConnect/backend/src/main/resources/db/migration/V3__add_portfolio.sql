-- Portfolio items for artists and fans
CREATE TABLE portfolio_item (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    media_url VARCHAR(1000),
    media_type VARCHAR(20),
    tags VARCHAR(500),
    visibility VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_portfolio_user_id ON portfolio_item(user_id);
CREATE INDEX idx_portfolio_category ON portfolio_item(category);
CREATE INDEX idx_portfolio_sort ON portfolio_item(sort_order);

