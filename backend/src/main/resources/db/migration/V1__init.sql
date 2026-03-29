CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE mood_options (
    id UUID PRIMARY KEY,
    label VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE reason_options (
    id UUID PRIMARY KEY,
    label VARCHAR(120) NOT NULL,
    requires_text BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE batch_texture_options (
    id UUID PRIMARY KEY,
    label VARCHAR(160) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE batch_smell_options (
    id UUID PRIMARY KEY,
    label VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

INSERT INTO users (id, username, password_hash, created_at, updated_at, is_active)
VALUES (
    gen_random_uuid(),
    'admin',
    '$2a$10$Qv7Kia1C/VSqrLuHDzAUz.TPo.wghb6L1.d5WwK/muMIr1oBXr6.m',
    NOW(),
    NOW(),
    TRUE
);

INSERT INTO mood_options (id, label, active, sort_order, created_at, updated_at) VALUES
    (gen_random_uuid(), 'Feliz', TRUE, 1, NOW(), NOW()),
    (gen_random_uuid(), 'Tranquilo', TRUE, 2, NOW(), NOW()),
    (gen_random_uuid(), 'Ansioso', TRUE, 3, NOW(), NOW()),
    (gen_random_uuid(), 'Com Raiva', TRUE, 4, NOW(), NOW()),
    (gen_random_uuid(), 'Triste', TRUE, 5, NOW(), NOW()),
    (gen_random_uuid(), 'Empolgado', TRUE, 6, NOW(), NOW()),
    (gen_random_uuid(), 'Cansado', TRUE, 7, NOW(), NOW()),
    (gen_random_uuid(), 'Desmotivado', TRUE, 8, NOW(), NOW()),
    (gen_random_uuid(), 'Caos Mental', TRUE, 9, NOW(), NOW());

INSERT INTO reason_options (id, label, requires_text, active, sort_order, created_at, updated_at) VALUES
    (gen_random_uuid(), 'Tedio', FALSE, TRUE, 1, NOW(), NOW()),
    (gen_random_uuid(), 'Empolgado', FALSE, TRUE, 2, NOW(), NOW()),
    (gen_random_uuid(), 'Triste', FALSE, TRUE, 3, NOW(), NOW()),
    (gen_random_uuid(), 'Vontade', FALSE, TRUE, 4, NOW(), NOW()),
    (gen_random_uuid(), 'Ansioso', FALSE, TRUE, 5, NOW(), NOW()),
    (gen_random_uuid(), 'Querer Mais', FALSE, TRUE, 6, NOW(), NOW()),
    (gen_random_uuid(), 'Caos Mental', FALSE, TRUE, 7, NOW(), NOW()),
    (gen_random_uuid(), 'Outros', TRUE, TRUE, 8, NOW(), NOW());

INSERT INTO batch_texture_options (id, label, active, sort_order, created_at, updated_at) VALUES
    (gen_random_uuid(), 'Arenoso', TRUE, 1, NOW(), NOW()),
    (gen_random_uuid(), 'Muito seco, duro de manipular', TRUE, 2, NOW(), NOW()),
    (gen_random_uuid(), 'Manipulavel', TRUE, 3, NOW(), NOW()),
    (gen_random_uuid(), 'Umido, mas manipulavel', TRUE, 4, NOW(), NOW()),
    (gen_random_uuid(), 'Muito umido, de dificil manipulacao', TRUE, 5, NOW(), NOW());

INSERT INTO batch_smell_options (id, label, active, sort_order, created_at, updated_at) VALUES
    (gen_random_uuid(), 'Cheiroso', TRUE, 1, NOW(), NOW()),
    (gen_random_uuid(), 'Muito cheiroso', TRUE, 2, NOW(), NOW()),
    (gen_random_uuid(), 'Quase sem cheiro', TRUE, 3, NOW(), NOW()),
    (gen_random_uuid(), 'Sem cheiro', TRUE, 4, NOW(), NOW());
