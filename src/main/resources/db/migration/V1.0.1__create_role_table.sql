CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,  
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Insert default roles
INSERT INTO roles (name, description) VALUES
    ('USER', 'Regular user with basic permissions'),
    ('MODERATOR', 'Moderator with content management permissions'),
    ('ADMIN', 'Administrator with full permissions')
ON CONFLICT (name) DO NOTHING;