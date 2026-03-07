-- AlterTable
ALTER TABLE "merch_items" ADD COLUMN     "has_sizes" BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN     "stock_sizes" JSONB;
