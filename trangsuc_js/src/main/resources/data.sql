-- Check if roles already exist before inserting
INSERT INTO roles (name) 
SELECT 'ROLE_USER' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name) 
SELECT 'ROLE_ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name) 
SELECT 'ROLE_STAFF' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_STAFF');

-- Insert categories
INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at) 
SELECT 'Nhẫn', 'nhan', 'Danh mục nhẫn vàng, bạc, kim cương', 'https://example.com/rings.jpg', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'nhan');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at) 
SELECT 'Bông tai', 'bong-tai', 'Danh mục bông tai đẹp', 'https://example.com/earrings.jpg', true, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'bong-tai');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at) 
SELECT 'Vòng tay', 'vong-tay', 'Danh mục vòng tay charm', 'https://example.com/bracelets.jpg', true, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'vong-tay');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at) 
SELECT 'Dây chuyền', 'day-chuyen', 'Danh mục dây chuyền cao cấp', 'https://example.com/necklaces.jpg', true, 4, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'day-chuyen');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at) 
SELECT 'Lắc tay', 'lac-tay', 'Danh mục lắc tay đẹp', 'https://example.com/anklets.jpg', true, 5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'lac-tay');