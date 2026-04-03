CREATE TABLE IF NOT EXISTS scn_files (
    id         TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    band_id    TEXT        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    name       TEXT        NOT NULL,
    content    TEXT        NOT NULL,
    notes      TEXT,
    gig_id     TEXT        REFERENCES gigs(id) ON DELETE SET NULL,
    venue      TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
