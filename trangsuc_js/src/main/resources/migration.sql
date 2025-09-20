-- Migration script to add is_on_sale column to products table
-- This script will be executed manually or through database migration tool

-- Add is_on_sale column to products table if it doesn't exist
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS is_on_sale BOOLEAN DEFAULT FALSE;

-- Update existing products to set is_on_sale based on sale_price
UPDATE products 
SET is_on_sale = TRUE 
WHERE sale_price IS NOT NULL AND sale_price < price;

-- Add index for better performance on is_on_sale queries
CREATE INDEX IF NOT EXISTS idx_products_is_on_sale ON products(is_on_sale);
CREATE INDEX IF NOT EXISTS idx_products_is_featured ON products(is_featured);
CREATE INDEX IF NOT EXISTS idx_products_is_new ON products(is_new);
CREATE INDEX IF NOT EXISTS idx_products_is_bestseller ON products(is_bestseller);
