-- CreateTable
CREATE TABLE "merch_waiting_list" (
    "id" TEXT NOT NULL,
    "band_id" TEXT NOT NULL,
    "item_id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "size" TEXT,
    "contact" TEXT,
    "notes" TEXT,
    "status" TEXT NOT NULL DEFAULT 'waiting',
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "merch_waiting_list_pkey" PRIMARY KEY ("id")
);

-- AddForeignKey
ALTER TABLE "merch_waiting_list" ADD CONSTRAINT "merch_waiting_list_band_id_fkey"
    FOREIGN KEY ("band_id") REFERENCES "bands"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "merch_waiting_list" ADD CONSTRAINT "merch_waiting_list_item_id_fkey"
    FOREIGN KEY ("item_id") REFERENCES "merch_items"("id") ON DELETE CASCADE ON UPDATE CASCADE;
