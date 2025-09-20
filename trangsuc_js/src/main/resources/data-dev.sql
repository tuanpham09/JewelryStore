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
SELECT 'Nhẫn vàng 24K', 'nhan-vang-24k', 'Nhẫn vàng 24K cao cấp, thiết kế tinh tế với hoa văn cổ điển. Sản phẩm được chế tác thủ công bởi những nghệ nhân lành nghề nhất.', 10000000, 10, 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', c.id
FROM categories c
WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-vang-24k');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Dây chuyền bạc 925', 'day-chuyen-bac-925', 'Dây chuyền bạc 925 cao cấp, thiết kế hiện đại với mặt dây tinh tế. Phù hợp cho mọi dịp đặc biệt.', 2500000, 15, 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id
FROM categories c
WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-bac-925');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Lắc tay vàng hồng', 'lac-tay-vang-hong', 'Lắc tay vàng hồng 18K, thiết kế sang trọng với những viên đá quý nhỏ xinh. Tạo điểm nhấn hoàn hảo cho cổ tay.', 8500000, 8, 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', c.id
FROM categories c
WHERE c.slug = 'lac-tay'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'lac-tay-vang-hong');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Bông tai kim cương', 'bong-tai-kim-cuong', 'Bông tai kim cương tự nhiên, thiết kế độc đáo với những viên kim cương được cắt tỉa hoàn hảo. Sản phẩm cao cấp nhất của chúng tôi.', 25000000, 5, 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', c.id
FROM categories c
WHERE c.slug = 'bong-tai'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'bong-tai-kim-cuong');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Nhẫn cưới vàng trắng', 'nhan-cuoi-vang-trang', 'Nhẫn cưới vàng trắng 18K với thiết kế tối giản nhưng sang trọng. Biểu tượng của tình yêu vĩnh cửu.', 12000000, 12, 'https://images.unsplash.com/photo-1603561596112-db0a0b8a0b8a?w=400&h=400&fit=crop', c.id
FROM categories c
WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-cuoi-vang-trang');

INSERT INTO products (name, slug, description, price, stock, thumbnail, category_id)
SELECT 'Dây chuyền ngọc trai', 'day-chuyen-ngoc-trai', 'Dây chuyền ngọc trai tự nhiên với thiết kế cổ điển. Vẻ đẹp tinh tế và thanh lịch cho phụ nữ hiện đại.', 18000000, 6, 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id
FROM categories c
WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-ngoc-trai');
