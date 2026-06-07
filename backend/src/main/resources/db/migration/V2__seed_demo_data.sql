-- Seed demo user for Phase 1 development (password: password)
INSERT INTO users (first_name, last_name, email, password, created_at, updated_at)
VALUES (
    'Demo',
    'User',
    'demo@bondkeeper.app',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    NOW(),
    NOW()
);

INSERT INTO priority_levels (level_name, reminder_frequency_days, color_code, user_id, created_at, updated_at)
VALUES
    ('High',   7,  '#EF4444', 1, NOW(), NOW()),
    ('Medium', 14, '#F59E0B', 1, NOW(), NOW()),
    ('Low',    30, '#10B981', 1, NOW(), NOW());

INSERT INTO categories (name, description, user_id, created_at, updated_at)
VALUES
    ('Family',   'Close family members',           1, NOW(), NOW()),
    ('Friends',  'Personal friends',             1, NOW(), NOW()),
    ('Mentors',  'Professional mentors & guides',  1, NOW(), NOW());
