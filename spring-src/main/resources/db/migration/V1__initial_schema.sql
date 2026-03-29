-- ============================================================
-- V1 — Initial schema (mirrors Prisma schema exactly)
-- This migration is SKIPPED on existing databases via Flyway
-- baseline-on-migrate=true / baseline-version=1
-- ============================================================

-- ── Core ──────────────────────────────────────────────────────

CREATE TABLE bands (
    id         UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT        NOT NULL,
    slug       TEXT        NOT NULL UNIQUE,
    logo       TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE users (
    id            UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    username      TEXT        NOT NULL UNIQUE,
    display_name  TEXT,
    password_hash TEXT        NOT NULL,
    role          TEXT        NOT NULL DEFAULT 'member',
    is_active     BOOLEAN     NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    band_id       UUID        REFERENCES bands(id) ON DELETE SET NULL
);

CREATE TABLE user_bands (
    id      UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    band_id UUID NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    role    TEXT NOT NULL DEFAULT 'member',
    CONSTRAINT uq_user_bands UNIQUE (user_id, band_id)
);

CREATE TABLE settings (
    id      UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id UUID NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    key     TEXT NOT NULL,
    value   TEXT NOT NULL,
    CONSTRAINT uq_settings_band_key UNIQUE (band_id, key)
);

-- ── Songs & Playlists ─────────────────────────────────────────

CREATE TABLE library_songs (
    id         UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id    UUID NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    title      TEXT NOT NULL,
    artist     TEXT NOT NULL,
    album      TEXT,
    duration   INTEGER,
    tempo      INTEGER,
    style      TEXT,
    notes      TEXT,
    start_note TEXT,
    end_note   TEXT
);

CREATE TABLE playlists (
    id          UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id     UUID        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    name        TEXT        NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE playlist_songs (
    id             UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    playlist_id    UUID    NOT NULL REFERENCES playlists(id) ON DELETE CASCADE,
    song_id        UUID    REFERENCES library_songs(id) ON DELETE SET NULL,
    position       INTEGER NOT NULL,
    type           TEXT    DEFAULT 'song',
    title          TEXT,
    setlist_name   TEXT,
    join_with_next BOOLEAN NOT NULL DEFAULT false
);

-- ── Equipment ─────────────────────────────────────────────────

CREATE TABLE band_members (
    id             UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id        UUID    NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    name           TEXT    NOT NULL,
    roles          TEXT    NOT NULL DEFAULT '[]',
    stage_position TEXT,
    vocal_mic_id   UUID,
    notes          TEXT,
    sort_order     INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE microphones (
    id               UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id          UUID    NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    name             TEXT    NOT NULL,
    brand            TEXT,
    model            TEXT,
    type             TEXT    NOT NULL,
    polar_pattern    TEXT,
    phantom_power    BOOLEAN NOT NULL DEFAULT false,
    mono_stereo      TEXT    NOT NULL DEFAULT 'mono',
    notes            TEXT,
    usage            TEXT,
    assigned_to_type TEXT,
    assigned_to_id   UUID
);

CREATE TABLE instruments (
    id            UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id       UUID    NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    member_id     UUID    REFERENCES band_members(id) ON DELETE SET NULL,
    name          TEXT    NOT NULL,
    type          TEXT    NOT NULL,
    brand         TEXT,
    model         TEXT,
    routing       TEXT    NOT NULL,
    amp_id        UUID,
    mono_stereo   TEXT,
    channel_order INTEGER NOT NULL DEFAULT 0,
    notes         TEXT
);

CREATE TABLE amplifiers (
    id             UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id        UUID    NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    member_id      UUID    REFERENCES band_members(id) ON DELETE SET NULL,
    name           TEXT    NOT NULL,
    type           TEXT    NOT NULL,
    brand          TEXT,
    model          TEXT,
    wattage        INTEGER,
    routing        TEXT    NOT NULL,
    mono_stereo    TEXT,
    stage_position TEXT,
    notes          TEXT,
    cabinet_brand  TEXT,
    speaker_brand  TEXT,
    speaker_model  TEXT,
    speaker_config TEXT
);

CREATE TABLE pa_equipment (
    id           UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id      UUID    NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    category     TEXT    NOT NULL,
    name         TEXT    NOT NULL,
    brand        TEXT,
    model        TEXT,
    quantity     INTEGER NOT NULL DEFAULT 1,
    channels     INTEGER,
    aux_sends    INTEGER,
    wattage      INTEGER,
    notes        TEXT,
    monitor_type TEXT,
    iem_wireless BOOLEAN NOT NULL DEFAULT false
);

-- ── Venues & Gigs ─────────────────────────────────────────────

CREATE TABLE venues (
    id             UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id        UUID        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    name           TEXT        NOT NULL,
    city           TEXT,
    address        TEXT,
    website        TEXT,
    capacity       INTEGER,
    booking_name   TEXT,
    booking_email  TEXT,
    booking_phone  TEXT,
    notes          TEXT,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE gigs (
    id              UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id         UUID        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    venue_id        UUID        REFERENCES venues(id) ON DELETE SET NULL,
    setlist_id      UUID        REFERENCES playlists(id) ON DELETE SET NULL,
    title           TEXT        NOT NULL,
    date            TEXT,
    time            TEXT,
    status          TEXT        NOT NULL DEFAULT 'lead',
    pay             TEXT,
    load_in_time    TEXT,
    soundcheck_time TEXT,
    set_time        TEXT,
    notes           TEXT,
    attendance      INTEGER,
    follow_up_date  TEXT,
    follow_up_note  TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE gig_contacts (
    id           UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    gig_id       UUID        NOT NULL REFERENCES gigs(id) ON DELETE CASCADE,
    date         TEXT        NOT NULL,
    contact_type TEXT        NOT NULL,
    notes        TEXT,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE gig_checklists (
    id         UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    gig_id     UUID        NOT NULL REFERENCES gigs(id) ON DELETE CASCADE,
    name       TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE checklist_items (
    id           UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    checklist_id UUID    NOT NULL REFERENCES gig_checklists(id) ON DELETE CASCADE,
    category     TEXT    NOT NULL DEFAULT 'otro',
    text         TEXT    NOT NULL,
    done         BOOLEAN NOT NULL DEFAULT false,
    sort_order   INTEGER NOT NULL DEFAULT 0
);

-- ── Calendar ──────────────────────────────────────────────────

CREATE TABLE calendar_events (
    id         UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id    UUID        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    member_id  UUID        REFERENCES band_members(id) ON DELETE SET NULL,
    type       TEXT        NOT NULL,
    title      TEXT        NOT NULL,
    date       TEXT        NOT NULL,
    end_date   TEXT,
    all_day    BOOLEAN     NOT NULL DEFAULT true,
    notes      TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ── Finance ───────────────────────────────────────────────────

CREATE TABLE transactions (
    id          UUID             NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id     UUID             NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    type        TEXT             NOT NULL,
    category    TEXT             NOT NULL,
    amount      DOUBLE PRECISION NOT NULL,
    date        TEXT             NOT NULL,
    description TEXT,
    gig_id      UUID             REFERENCES gigs(id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ      NOT NULL DEFAULT NOW()
);

CREATE TABLE wish_list_items (
    id              UUID             NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id         UUID             NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    name            TEXT             NOT NULL,
    category        TEXT             NOT NULL,
    estimated_price DOUBLE PRECISION,
    priority        TEXT             NOT NULL DEFAULT 'medium',
    notes           TEXT,
    purchased       BOOLEAN          NOT NULL DEFAULT false,
    created_at      TIMESTAMPTZ      NOT NULL DEFAULT NOW()
);

-- ── Rehearsals ────────────────────────────────────────────────

CREATE TABLE rehearsals (
    id         UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id    UUID        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    date       TEXT        NOT NULL,
    notes      TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE rehearsal_songs (
    id           UUID    NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    rehearsal_id UUID    NOT NULL REFERENCES rehearsals(id) ON DELETE CASCADE,
    song_id      UUID    NOT NULL REFERENCES library_songs(id) ON DELETE CASCADE,
    notes        TEXT,
    rating       INTEGER
);

-- ── Merch ─────────────────────────────────────────────────────

CREATE TABLE merch_items (
    id              UUID             NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id         UUID             NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    name            TEXT             NOT NULL,
    type            TEXT             NOT NULL,
    production_cost DOUBLE PRECISION NOT NULL,
    batch_size      INTEGER          NOT NULL DEFAULT 50,
    selling_price   DOUBLE PRECISION NOT NULL,
    fixed_costs     DOUBLE PRECISION NOT NULL DEFAULT 0,
    stock           INTEGER          NOT NULL DEFAULT 0,
    has_sizes       BOOLEAN          NOT NULL DEFAULT false,
    stock_sizes     JSONB,
    notes           TEXT,
    created_at      TIMESTAMPTZ      NOT NULL DEFAULT NOW()
);

CREATE TABLE merch_waiting_list (
    id         UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id    UUID        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    item_id    UUID        NOT NULL REFERENCES merch_items(id) ON DELETE CASCADE,
    name       TEXT        NOT NULL,
    quantity   INTEGER     NOT NULL DEFAULT 1,
    size       TEXT,
    contact    TEXT,
    notes      TEXT,
    status     TEXT        NOT NULL DEFAULT 'waiting',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ── Voting ────────────────────────────────────────────────────

CREATE TABLE vote_sessions (
    id          UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    band_id     UUID        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    playlist_id UUID        NOT NULL REFERENCES playlists(id) ON DELETE CASCADE,
    title       TEXT        NOT NULL,
    status      TEXT        NOT NULL DEFAULT 'open',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE votes (
    id           UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    session_id   UUID        NOT NULL REFERENCES vote_sessions(id) ON DELETE CASCADE,
    voter_name   TEXT        NOT NULL,
    ordered_ids  TEXT        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_votes_session_voter UNIQUE (session_id, voter_name)
);
