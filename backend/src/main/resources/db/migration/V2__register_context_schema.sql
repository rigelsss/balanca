CREATE TABLE day_logs (
    id UUID PRIMARY KEY,
    day DATE NOT NULL UNIQUE,
    has_first_use BOOLEAN NOT NULL DEFAULT FALSE,
    ran_today BOOLEAN NOT NULL DEFAULT FALSE,
    sleep_quality SMALLINT NULL,
    sleep_hours SMALLINT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE batches (
    id UUID PRIMARY KEY,
    label VARCHAR(160) NOT NULL,
    start_date DATE NOT NULL,
    grams NUMERIC(12, 4) NULL,
    batch_value NUMERIC(12, 2) NULL,
    value_per_gram NUMERIC(12, 6) NULL,
    texture_option_id UUID NULL REFERENCES batch_texture_options(id),
    texture_label_snapshot VARCHAR(160) NULL,
    smell_option_id UUID NULL REFERENCES batch_smell_options(id),
    smell_label_snapshot VARCHAR(160) NULL,
    notes TEXT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE use_entries (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    day_log_id UUID NOT NULL REFERENCES day_logs(id),
    mood_option_id UUID NULL REFERENCES mood_options(id),
    mood_label_snapshot VARCHAR(120) NULL,
    reason_option_id UUID NULL REFERENCES reason_options(id),
    reason_label_snapshot VARCHAR(120) NULL,
    reason_text TEXT NULL,
    first_of_day BOOLEAN NOT NULL DEFAULT FALSE,
    away_long BOOLEAN NOT NULL DEFAULT FALSE,
    ran_today_snapshot BOOLEAN NOT NULL DEFAULT FALSE,
    sleep_quality_snapshot SMALLINT NULL,
    sleep_hours_snapshot SMALLINT NULL,
    batch_id UUID NULL REFERENCES batches(id),
    batch_label_snapshot VARCHAR(160) NULL,
    measure_kind VARCHAR(16) NOT NULL,
    stable_ok BOOLEAN NULL,
    stable_span_g NUMERIC(12, 6) NULL,
    weight_g NUMERIC(12, 6) NOT NULL,
    serial_line TEXT NULL,
    serial_port VARCHAR(120) NULL,
    serial_baud INTEGER NULL,
    app_version VARCHAR(40) NULL,
    notes TEXT NULL
);

CREATE INDEX idx_use_entries_day_log_id ON use_entries(day_log_id);
CREATE INDEX idx_use_entries_created_at ON use_entries(created_at);
CREATE INDEX idx_batches_active_created_at ON batches(active, created_at DESC);
