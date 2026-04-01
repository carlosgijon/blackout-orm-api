CREATE TABLE IF NOT EXISTS rehearsals (
    id         TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    band_id    TEXT        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    date       TEXT        NOT NULL,
    notes      TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS rehearsal_songs (
    id           TEXT    NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    rehearsal_id TEXT    NOT NULL REFERENCES rehearsals(id) ON DELETE CASCADE,
    song_id      TEXT    NOT NULL REFERENCES library_songs(id) ON DELETE CASCADE,
    notes        TEXT,
    rating       INTEGER
);

CREATE TABLE IF NOT EXISTS vote_sessions (
    id          TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    band_id     TEXT        NOT NULL REFERENCES bands(id) ON DELETE CASCADE,
    playlist_id TEXT        NOT NULL REFERENCES playlists(id) ON DELETE CASCADE,
    title       TEXT        NOT NULL,
    status      TEXT        NOT NULL DEFAULT 'open',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS votes (
    id           TEXT        NOT NULL DEFAULT gen_random_uuid()::text PRIMARY KEY,
    session_id   TEXT        NOT NULL REFERENCES vote_sessions(id) ON DELETE CASCADE,
    voter_name   TEXT        NOT NULL,
    ordered_ids  TEXT        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_votes_session_voter UNIQUE (session_id, voter_name)
);
