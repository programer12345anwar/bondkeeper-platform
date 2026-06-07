-- BondKeeper initial schema
-- PostgreSQL 15+

CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100)  NOT NULL,
    last_name       VARCHAR(100)  NOT NULL,
    email           VARCHAR(255)  NOT NULL UNIQUE,
    password        VARCHAR(255)  NOT NULL,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE TABLE categories (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100)  NOT NULL,
    description     TEXT,
    user_id         BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_category_name_per_user UNIQUE (user_id, name)
);

CREATE TABLE priority_levels (
    id                          BIGSERIAL PRIMARY KEY,
    level_name                  VARCHAR(100)  NOT NULL,
    reminder_frequency_days     INTEGER       NOT NULL CHECK (reminder_frequency_days > 0),
    color_code                  VARCHAR(7)    NOT NULL,
    user_id                     BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at                  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_priority_level_name_per_user UNIQUE (user_id, level_name)
);

CREATE TABLE contacts (
    id                      BIGSERIAL PRIMARY KEY,
    name                    VARCHAR(200)  NOT NULL,
    phone_number            VARCHAR(30),
    whatsapp_number         VARCHAR(30),
    notes                   TEXT,
    relationship_type       VARCHAR(50)   NOT NULL,
    relationship_score      INTEGER       NOT NULL DEFAULT 50 CHECK (relationship_score BETWEEN 0 AND 100),
    last_interaction_date   DATE,
    inner_circle            BOOLEAN       NOT NULL DEFAULT FALSE,
    category_id             BIGINT        REFERENCES categories(id) ON DELETE SET NULL,
    priority_level_id       BIGINT        REFERENCES priority_levels(id) ON DELETE SET NULL,
    user_id                 BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at              TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE TABLE interactions (
    id                  BIGSERIAL PRIMARY KEY,
    interaction_type    VARCHAR(50)   NOT NULL,
    interaction_date    DATE          NOT NULL,
    notes               TEXT,
    contact_id          BIGINT        NOT NULL REFERENCES contacts(id) ON DELETE CASCADE,
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE TABLE reminders (
    id                  BIGSERIAL PRIMARY KEY,
    reminder_message    TEXT          NOT NULL,
    reminder_type       VARCHAR(50)   NOT NULL,
    reminder_date       DATE          NOT NULL,
    contact_id          BIGINT        NOT NULL REFERENCES contacts(id) ON DELETE CASCADE,
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

-- Indexes for common query patterns
CREATE INDEX idx_categories_user_id ON categories(user_id);
CREATE INDEX idx_priority_levels_user_id ON priority_levels(user_id);
CREATE INDEX idx_contacts_user_id ON contacts(user_id);
CREATE INDEX idx_contacts_category_id ON contacts(category_id);
CREATE INDEX idx_contacts_priority_level_id ON contacts(priority_level_id);
CREATE INDEX idx_contacts_inner_circle ON contacts(user_id, inner_circle) WHERE inner_circle = TRUE;
CREATE INDEX idx_interactions_contact_id ON interactions(contact_id);
CREATE INDEX idx_interactions_date ON interactions(interaction_date DESC);
CREATE INDEX idx_reminders_contact_id ON reminders(contact_id);
CREATE INDEX idx_reminders_date ON reminders(reminder_date);
