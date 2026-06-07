-- Phase 2: roles and refresh tokens

ALTER TABLE users ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER';

CREATE TABLE refresh_tokens (
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(512)  NOT NULL UNIQUE,
    user_id     BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expires_at  TIMESTAMPTZ   NOT NULL,
    revoked     BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);

UPDATE users SET role = 'ROLE_USER' WHERE email = 'demo@bondkeeper.app';
