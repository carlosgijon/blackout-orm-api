-- CreateTable
CREATE TABLE "rehearsals" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "date" TEXT NOT NULL,
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "rehearsals_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "rehearsal_songs" (
    "id" TEXT NOT NULL,
    "rehearsal_id" TEXT NOT NULL,
    "song_id" TEXT NOT NULL,
    "notes" TEXT,
    "rating" INTEGER,

    CONSTRAINT "rehearsal_songs_pkey" PRIMARY KEY ("id")
);

-- AddForeignKey
ALTER TABLE "rehearsals" ADD CONSTRAINT "rehearsals_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "rehearsal_songs" ADD CONSTRAINT "rehearsal_songs_rehearsal_id_fkey" FOREIGN KEY ("rehearsal_id") REFERENCES "rehearsals"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "rehearsal_songs" ADD CONSTRAINT "rehearsal_songs_song_id_fkey" FOREIGN KEY ("song_id") REFERENCES "library_songs"("id") ON DELETE CASCADE ON UPDATE CASCADE;
