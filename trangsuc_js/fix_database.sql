-- Fix database schema for orders table
USE jewelry;

-- Add default values for orders table
ALTER TABLE orders MODIFY COLUMN total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00;
ALTER TABLE orders MODIFY COLUMN subtotal DECIMAL(10,2) DEFAULT 0.00;
ALTER TABLE orders MODIFY COLUMN shipping_fee DECIMAL(10,2) DEFAULT 0.00;
ALTER TABLE orders MODIFY COLUMN discount_amount DECIMAL(10,2) DEFAULT 0.00;

-- Add default values for order_items table
ALTER TABLE order_items MODIFY COLUMN quantity INT NOT NULL DEFAULT 0;
ALTER TABLE order_items MODIFY COLUMN unit_price DECIMAL(10,2) NOT NULL DEFAULT 0.00;
ALTER TABLE order_items MODIFY COLUMN subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00;

-- Show the updated table structure
DESCRIBE orders;
DESCRIBE order_items;
