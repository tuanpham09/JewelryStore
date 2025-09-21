-- Fix database schema for price columns in products table
-- Increase precision for original_price and sale_price columns

USE jewelry;

-- Modify original_price column to support larger values
-- Change from DECIMAL(10,2) to DECIMAL(15,2) to support up to 13 digits before decimal
ALTER TABLE products MODIFY COLUMN original_price DECIMAL(15,2);

-- Modify sale_price column to support larger values  
-- Change from DECIMAL(10,2) to DECIMAL(15,2) to support up to 13 digits before decimal
ALTER TABLE products MODIFY COLUMN sale_price DECIMAL(15,2);

-- Also modify price column for consistency
ALTER TABLE products MODIFY COLUMN price DECIMAL(15,2);

-- Show the updated table structure
DESCRIBE products;
