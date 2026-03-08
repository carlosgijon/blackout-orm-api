-- AlterTable
ALTER TABLE "gigs" ADD COLUMN     "attendance" INTEGER;

-- AlterTable
ALTER TABLE "library_songs" ADD COLUMN     "end_note" TEXT,
ADD COLUMN     "start_note" TEXT;
