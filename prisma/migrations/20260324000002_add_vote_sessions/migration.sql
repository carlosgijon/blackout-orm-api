-- CreateTable: vote_sessions
CREATE TABLE "vote_sessions" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "playlist_id" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "status" TEXT NOT NULL DEFAULT 'open',
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "vote_sessions_pkey" PRIMARY KEY ("id")
);

-- CreateTable: votes
CREATE TABLE "votes" (
    "id" TEXT NOT NULL,
    "session_id" TEXT NOT NULL,
    "voter_name" TEXT NOT NULL,
    "ordered_ids" TEXT NOT NULL,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "votes_pkey" PRIMARY KEY ("id")
);

-- CreateIndex: unique vote per voter per session
CREATE UNIQUE INDEX "votes_session_id_voter_name_key" ON "votes"("session_id", "voter_name");

-- AddForeignKey: vote_sessions → bands
ALTER TABLE "vote_sessions" ADD CONSTRAINT "vote_sessions_band_id_fkey"
    FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey: vote_sessions → playlists
ALTER TABLE "vote_sessions" ADD CONSTRAINT "vote_sessions_playlist_id_fkey"
    FOREIGN KEY ("playlist_id") REFERENCES "playlists"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey: votes → vote_sessions
ALTER TABLE "votes" ADD CONSTRAINT "votes_session_id_fkey"
    FOREIGN KEY ("session_id") REFERENCES "vote_sessions"("id") ON DELETE CASCADE ON UPDATE CASCADE;
