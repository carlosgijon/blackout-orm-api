CREATE TABLE IF NOT EXISTS polls (
    id             TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    band_id        TEXT        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    title          TEXT        NOT NULL,
    description    TEXT,
    type           TEXT        NOT NULL,
    status         TEXT        NOT NULL DEFAULT 'draft',
    created_by     TEXT        NOT NULL,
    deadline       TEXT,
    linked_gig_id  TEXT        REFERENCES gigs(id) ON DELETE SET NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS poll_options (
    id           TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    poll_id      TEXT        NOT NULL REFERENCES polls(id) ON DELETE CASCADE,
    text         TEXT        NOT NULL,
    proposed_by  TEXT,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS poll_votes (
    id           TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    poll_id      TEXT        NOT NULL REFERENCES polls(id) ON DELETE CASCADE,
    option_id    TEXT        REFERENCES poll_options(id) ON DELETE CASCADE,
    voter_name   TEXT        NOT NULL,
    value        TEXT        NOT NULL,
    comment      TEXT,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
