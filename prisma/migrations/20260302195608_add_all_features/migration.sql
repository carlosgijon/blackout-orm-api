-- CreateTable
CREATE TABLE "settings" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "key" TEXT NOT NULL,
    "value" TEXT NOT NULL,

    CONSTRAINT "settings_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "library_songs" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "artist" TEXT NOT NULL,
    "album" TEXT,
    "duration" INTEGER,
    "tempo" INTEGER,
    "style" TEXT,
    "notes" TEXT,

    CONSTRAINT "library_songs_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "playlists" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "description" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "playlists_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "playlist_songs" (
    "id" TEXT NOT NULL,
    "playlist_id" TEXT NOT NULL,
    "song_id" TEXT,
    "position" INTEGER NOT NULL,
    "type" TEXT DEFAULT 'song',
    "title" TEXT,
    "setlist_name" TEXT,
    "join_with_next" BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT "playlist_songs_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "band_members" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "roles" TEXT NOT NULL DEFAULT '[]',
    "stage_position" TEXT,
    "vocal_mic_id" TEXT,
    "notes" TEXT,
    "sort_order" INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT "band_members_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "microphones" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "brand" TEXT,
    "model" TEXT,
    "type" TEXT NOT NULL,
    "polar_pattern" TEXT,
    "phantom_power" BOOLEAN NOT NULL DEFAULT false,
    "mono_stereo" TEXT NOT NULL DEFAULT 'mono',
    "notes" TEXT,
    "usage" TEXT,
    "assigned_to_type" TEXT,
    "assigned_to_id" TEXT,

    CONSTRAINT "microphones_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "instruments" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "member_id" TEXT,
    "name" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "brand" TEXT,
    "model" TEXT,
    "routing" TEXT NOT NULL,
    "amp_id" TEXT,
    "mono_stereo" TEXT,
    "channel_order" INTEGER NOT NULL DEFAULT 0,
    "notes" TEXT,

    CONSTRAINT "instruments_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "amplifiers" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "member_id" TEXT,
    "name" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "brand" TEXT,
    "model" TEXT,
    "wattage" INTEGER,
    "routing" TEXT NOT NULL,
    "mono_stereo" TEXT,
    "stage_position" TEXT,
    "notes" TEXT,
    "cabinet_brand" TEXT,
    "speaker_brand" TEXT,
    "speaker_model" TEXT,
    "speaker_config" TEXT,

    CONSTRAINT "amplifiers_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "pa_equipment" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "category" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "brand" TEXT,
    "model" TEXT,
    "quantity" INTEGER NOT NULL DEFAULT 1,
    "channels" INTEGER,
    "aux_sends" INTEGER,
    "wattage" INTEGER,
    "notes" TEXT,
    "monitor_type" TEXT,
    "iem_wireless" BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT "pa_equipment_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "venues" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "city" TEXT,
    "address" TEXT,
    "website" TEXT,
    "capacity" INTEGER,
    "booking_name" TEXT,
    "booking_email" TEXT,
    "booking_phone" TEXT,
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "venues_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "gigs" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "venue_id" TEXT,
    "setlist_id" TEXT,
    "title" TEXT NOT NULL,
    "date" TEXT,
    "time" TEXT,
    "status" TEXT NOT NULL DEFAULT 'lead',
    "pay" TEXT,
    "load_in_time" TEXT,
    "soundcheck_time" TEXT,
    "set_time" TEXT,
    "notes" TEXT,
    "follow_up_date" TEXT,
    "follow_up_note" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "gigs_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "gig_contacts" (
    "id" TEXT NOT NULL,
    "gig_id" TEXT NOT NULL,
    "date" TEXT NOT NULL,
    "contact_type" TEXT NOT NULL,
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "gig_contacts_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "calendar_events" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "member_id" TEXT,
    "type" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "date" TEXT NOT NULL,
    "end_date" TEXT,
    "all_day" BOOLEAN NOT NULL DEFAULT true,
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "calendar_events_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "gig_checklists" (
    "id" TEXT NOT NULL,
    "gig_id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "gig_checklists_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "checklist_items" (
    "id" TEXT NOT NULL,
    "checklist_id" TEXT NOT NULL,
    "category" TEXT NOT NULL DEFAULT 'otro',
    "text" TEXT NOT NULL,
    "done" BOOLEAN NOT NULL DEFAULT false,
    "sort_order" INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT "checklist_items_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "settings_band_id_key_key" ON "settings"("band_id", "key");

-- AddForeignKey
ALTER TABLE "settings" ADD CONSTRAINT "settings_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "library_songs" ADD CONSTRAINT "library_songs_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "playlists" ADD CONSTRAINT "playlists_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "playlist_songs" ADD CONSTRAINT "playlist_songs_playlist_id_fkey" FOREIGN KEY ("playlist_id") REFERENCES "playlists"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "playlist_songs" ADD CONSTRAINT "playlist_songs_song_id_fkey" FOREIGN KEY ("song_id") REFERENCES "library_songs"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "band_members" ADD CONSTRAINT "band_members_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "microphones" ADD CONSTRAINT "microphones_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "instruments" ADD CONSTRAINT "instruments_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "instruments" ADD CONSTRAINT "instruments_member_id_fkey" FOREIGN KEY ("member_id") REFERENCES "band_members"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "amplifiers" ADD CONSTRAINT "amplifiers_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "amplifiers" ADD CONSTRAINT "amplifiers_member_id_fkey" FOREIGN KEY ("member_id") REFERENCES "band_members"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "pa_equipment" ADD CONSTRAINT "pa_equipment_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "venues" ADD CONSTRAINT "venues_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "gigs" ADD CONSTRAINT "gigs_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "gigs" ADD CONSTRAINT "gigs_venue_id_fkey" FOREIGN KEY ("venue_id") REFERENCES "venues"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "gigs" ADD CONSTRAINT "gigs_setlist_id_fkey" FOREIGN KEY ("setlist_id") REFERENCES "playlists"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "gig_contacts" ADD CONSTRAINT "gig_contacts_gig_id_fkey" FOREIGN KEY ("gig_id") REFERENCES "gigs"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "calendar_events" ADD CONSTRAINT "calendar_events_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "calendar_events" ADD CONSTRAINT "calendar_events_member_id_fkey" FOREIGN KEY ("member_id") REFERENCES "band_members"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "gig_checklists" ADD CONSTRAINT "gig_checklists_gig_id_fkey" FOREIGN KEY ("gig_id") REFERENCES "gigs"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "checklist_items" ADD CONSTRAINT "checklist_items_checklist_id_fkey" FOREIGN KEY ("checklist_id") REFERENCES "gig_checklists"("id") ON DELETE CASCADE ON UPDATE CASCADE;
