-- AlterTable
ALTER TABLE "bands" ADD COLUMN     "logo" TEXT;

-- CreateTable
CREATE TABLE "user_bands" (
    "id" TEXT NOT NULL,
    "user_id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "role" TEXT NOT NULL DEFAULT 'member',

    CONSTRAINT "user_bands_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "user_bands_user_id_band_id_key" ON "user_bands"("user_id", "band_id");

-- AddForeignKey
ALTER TABLE "user_bands" ADD CONSTRAINT "user_bands_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "users"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "user_bands" ADD CONSTRAINT "user_bands_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;
