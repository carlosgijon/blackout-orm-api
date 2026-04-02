-- CreateTable
CREATE TABLE "polls" (
    "id"             TEXT        NOT NULL PRIMARY KEY,
    "band_id"        TEXT        NOT NULL REFERENCES "bands"("id") ON DELETE CASCADE,
    "title"          TEXT        NOT NULL,
    "description"    TEXT,
    "type"           TEXT        NOT NULL,
    "status"         TEXT        NOT NULL DEFAULT 'draft',
    "created_by"     TEXT        NOT NULL,
    "deadline"       TIMESTAMPTZ,
    "linked_gig_id"  TEXT        REFERENCES "gigs"("id") ON DELETE SET NULL,
    "created_at"     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- CreateTable
CREATE TABLE "poll_options" (
    "id"          TEXT        NOT NULL PRIMARY KEY,
    "poll_id"     TEXT        NOT NULL REFERENCES "polls"("id") ON DELETE CASCADE,
    "text"        TEXT        NOT NULL,
    "proposed_by" TEXT,
    "created_at"  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- CreateTable
CREATE TABLE "poll_votes" (
    "id"          TEXT        NOT NULL PRIMARY KEY,
    "poll_id"     TEXT        NOT NULL REFERENCES "polls"("id") ON DELETE CASCADE,
    "option_id"   TEXT        REFERENCES "poll_options"("id") ON DELETE CASCADE,
    "voter_name"  TEXT        NOT NULL,
    "value"       TEXT        NOT NULL,
    "comment"     TEXT,
    "created_at"  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- CreateIndex
CREATE INDEX "polls_band_id_idx"       ON "polls"("band_id");
CREATE INDEX "polls_linked_gig_id_idx" ON "polls"("linked_gig_id");
CREATE INDEX "poll_options_poll_id_idx" ON "poll_options"("poll_id");
CREATE INDEX "poll_votes_poll_id_idx"   ON "poll_votes"("poll_id");
CREATE INDEX "poll_votes_option_id_idx" ON "poll_votes"("option_id");
