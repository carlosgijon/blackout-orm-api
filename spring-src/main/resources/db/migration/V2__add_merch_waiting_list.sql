CREATE TABLE IF NOT EXISTS merch_waiting_list (
    id         TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    band_id    TEXT        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    item_id    TEXT        NOT NULL REFERENCES merch_items(id) ON DELETE CASCADE,
    name       TEXT        NOT NULL,
    quantity   INTEGER     NOT NULL DEFAULT 1,
    size       TEXT,
    contact    TEXT,
    notes      TEXT,
    status     TEXT        NOT NULL DEFAULT 'waiting',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
