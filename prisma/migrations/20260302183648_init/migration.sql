-- CreateTable
CREATE TABLE "bands" (
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "slug" TEXT NOT NULL,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "bands_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "users" (
    "id" TEXT NOT NULL,
    "username" TEXT NOT NULL,
    "display_name" TEXT,
    "password_hash" TEXT NOT NULL,
    "role" TEXT NOT NULL DEFAULT 'member',
    "is_active" BOOLEAN NOT NULL DEFAULT true,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "band_id" TEXT,

    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "bands_slug_key" ON "bands"("slug");

-- CreateIndex
CREATE UNIQUE INDEX "users_username_key" ON "users"("username");

-- AddForeignKey
ALTER TABLE "users" ADD CONSTRAINT "users_band_id_fkey" FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE SET NULL ON UPDATE CASCADE;
