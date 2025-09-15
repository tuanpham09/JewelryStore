-- Thêm dữ liệu mẫu cho môi trường dev

-- 1. Thêm roles (nếu chưa có)
INSERT INTO roles (name) 
SELECT 'ROLE_USER' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name) 
SELECT 'ROLE_ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name) 
SELECT 'ROLE_STAFF' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_STAFF');

-- 2. Thêm danh mục sản phẩm
INSERT INTO categories (name, slug)
SELECT 'Nhẫn', 'nhan'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'nhan');

INSERT INTO categories (name, slug)
SELECT 'Dây chuyền', 'day-chuyen'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'day-chuyen');

INSERT INTO categories (name, slug)
SELECT 'Lắc tay', 'lac-tay'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'lac-tay');

INSERT INTO categories (name, slug)
SELECT 'Bông tai', 'bong-tai'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'bong-tai');

-- 3. Thêm sản phẩm mẫu
INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Nhẫn vàng 24K', 'nhan-vang-24k', 'Nhẫn vàng 24K cao cấp, thiết kế tinh tế', 10000000, 10, 'nhan-vang-24k.jpg', c.id
FROM categories c
WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-vang-24k');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Dây chuyền bạc 925', 'day-chuyen-bac-925', 'Dây chuyền bạc 925 cao cấp, thiết kế hiện đại', 2500000, 15, 'day-chuyen-bac-925.jpg', c.id
FROM categories c
WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-bac-925');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Lắc tay vàng hồng', 'lac-tay-vang-hong', 'Lắc tay vàng hồng 18K, thiết kế sang trọng', 8500000, 8, 'lac-tay-vang-hong.jpg', c.id
FROM categories c
WHERE c.slug = 'lac-tay'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'lac-tay-vang-hong');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Bông tai kim cương', 'bong-tai-kim-cuong', 'Bông tai kim cương tự nhiên, thiết kế độc đáo', 25000000, 5, 'bong-tai-kim-cuong.jpg', c.id
FROM categories c
WHERE c.slug = 'bong-tai'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'bong-tai-kim-cuong');
