-- Thêm dữ liệu mẫu cho môi trường dev - COMPLETE DATASET

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

-- 2. Thêm users (admin + customers)
INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at, last_login_at)
SELECT 'admin@gmail.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Administrator', '0123456789', 'Admin Address', true, NOW() - INTERVAL 30 DAY, NOW(), NOW() - INTERVAL 1 DAY
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at, last_login_at)
SELECT 'customer1@gmail.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Nguyễn Văn An', '0987654321', '123 Đường ABC, Quận 1, TP.HCM', true, NOW() - INTERVAL 25 DAY, NOW(), NOW() - INTERVAL 2 HOUR
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'customer1@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at, last_login_at)
SELECT 'customer2@gmail.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Trần Thị Bình', '0987654322', '456 Đường DEF, Quận 2, TP.HCM', true, NOW() - INTERVAL 20 DAY, NOW(), NOW() - INTERVAL 1 DAY
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'customer2@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at, last_login_at)
SELECT 'customer3@gmail.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Lê Văn Cường', '0987654323', '789 Đường GHI, Quận 3, TP.HCM', true, NOW() - INTERVAL 15 DAY, NOW(), NOW() - INTERVAL 3 HOUR
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'customer3@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at, last_login_at)
SELECT 'customer4@gmail.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Phạm Thị Dung', '0987654324', '321 Đường JKL, Quận 4, TP.HCM', true, NOW() - INTERVAL 10 DAY, NOW(), NOW() - INTERVAL 6 HOUR
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'customer4@gmail.com');

INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at, last_login_at)
SELECT 'customer5@gmail.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Hoàng Văn Em', '0987654325', '654 Đường MNO, Quận 5, TP.HCM', true, NOW() - INTERVAL 5 DAY, NOW(), NOW() - INTERVAL 12 HOUR
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'customer5@gmail.com');

-- 3. Gán roles cho users
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@gmail.com' AND r.name = 'ROLE_ADMIN'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email IN ('customer1@gmail.com', 'customer2@gmail.com', 'customer3@gmail.com', 'customer4@gmail.com', 'customer5@gmail.com') 
AND r.name = 'ROLE_USER'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

-- 4. Thêm danh mục sản phẩm
INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at)
SELECT 'Nhẫn', 'nhan', 'Nhẫn vàng, nhẫn bạc, nhẫn kim cương cao cấp', 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', true, 1, NOW() - INTERVAL 30 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'nhan');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at)
SELECT 'Dây chuyền', 'day-chuyen', 'Dây chuyền vàng, dây chuyền bạc, dây chuyền ngọc trai', 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', true, 2, NOW() - INTERVAL 30 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'day-chuyen');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at)
SELECT 'Lắc tay', 'lac-tay', 'Lắc tay vàng, lắc tay bạc, lắc tay charm', 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', true, 3, NOW() - INTERVAL 30 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'lac-tay');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at)
SELECT 'Bông tai', 'bong-tai', 'Bông tai vàng, bông tai bạc, bông tai kim cương', 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', true, 4, NOW() - INTERVAL 30 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'bong-tai');

INSERT INTO categories (name, slug, description, image_url, is_active, sort_order, created_at, updated_at)
SELECT 'Vòng tay', 'vong-tay', 'Vòng tay vàng, vòng tay bạc, vòng tay charm', 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', true, 5, NOW() - INTERVAL 30 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'vong-tay');

-- 5. Thêm sản phẩm với dữ liệu đầy đủ
INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Nhẫn vàng 24K cao cấp', 'nhan-vang-24k-cao-cap', 'Nhẫn vàng 24K cao cấp với thiết kế tinh tế, hoa văn cổ điển. Sản phẩm được chế tác thủ công bởi những nghệ nhân lành nghề nhất.', 'Nhẫn vàng 24K thiết kế cổ điển', 15000000, 18000000, 15000000, 8, 2, 50, 'N24K-001', '1234567890123', 5.5, '18x8x3mm', 'Vàng 24K', 'Vàng', 'JewelryStore', 'Việt Nam', 24, true, true, false, true, 156, 12, 'Nhẫn vàng 24K cao cấp - JewelryStore', 'Nhẫn vàng 24K cao cấp với thiết kế tinh tế, hoa văn cổ điển', 'nhẫn vàng, nhẫn 24k, trang sức cao cấp', 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 25 DAY, NOW()
FROM categories c WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-vang-24k-cao-cap');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Dây chuyền bạc 925', 'day-chuyen-bac-925', 'Dây chuyền bạc 925 cao cấp, thiết kế hiện đại với mặt dây tinh tế. Phù hợp cho mọi dịp đặc biệt.', 'Dây chuyền bạc 925 thiết kế hiện đại', 3500000, 4200000, 3500000, 15, 3, 100, 'DC925-001', '1234567890124', 8.2, '45cm', 'Bạc 925', 'Bạc', 'JewelryStore', 'Việt Nam', 12, true, false, true, false, 89, 8, 'Dây chuyền bạc 925 - JewelryStore', 'Dây chuyền bạc 925 cao cấp, thiết kế hiện đại', 'dây chuyền bạc, trang sức bạc, dây chuyền 925', 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 20 DAY, NOW()
FROM categories c WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-bac-925');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Lắc tay vàng hồng 18K', 'lac-tay-vang-hong-18k', 'Lắc tay vàng hồng 18K, thiết kế sang trọng với những viên đá quý nhỏ xinh. Tạo điểm nhấn hoàn hảo cho cổ tay.', 'Lắc tay vàng hồng 18K sang trọng', 12000000, 12000000, null, 6, 2, 30, 'LT18K-001', '1234567890125', 12.5, '18cm', 'Vàng 18K', 'Vàng hồng', 'JewelryStore', 'Việt Nam', 18, true, true, false, true, 234, 15, 'Lắc tay vàng hồng 18K - JewelryStore', 'Lắc tay vàng hồng 18K, thiết kế sang trọng', 'lắc tay vàng, lắc tay 18k, trang sức vàng hồng', 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 18 DAY, NOW()
FROM categories c WHERE c.slug = 'lac-tay'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'lac-tay-vang-hong-18k');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Bông tai kim cương', 'bong-tai-kim-cuong', 'Bông tai kim cương tự nhiên, thiết kế độc đáo với những viên kim cương được cắt tỉa hoàn hảo. Sản phẩm cao cấp nhất của chúng tôi.', 'Bông tai kim cương tự nhiên cao cấp', 35000000, 42000000, 35000000, 3, 1, 20, 'BTKC-001', '1234567890126', 3.8, '15x8mm', 'Kim cương', 'Trắng', 'JewelryStore', 'Việt Nam', 36, true, true, false, true, 445, 5, 'Bông tai kim cương - JewelryStore', 'Bông tai kim cương tự nhiên, thiết kế độc đáo', 'bông tai kim cương, trang sức kim cương, bông tai cao cấp', 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 15 DAY, NOW()
FROM categories c WHERE c.slug = 'bong-tai'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'bong-tai-kim-cuong');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Nhẫn cưới vàng trắng 18K', 'nhan-cuoi-vang-trang-18k', 'Nhẫn cưới vàng trắng 18K với thiết kế tối giản nhưng sang trọng. Biểu tượng của tình yêu vĩnh cửu.', 'Nhẫn cưới vàng trắng 18K tối giản', 18000000, 18000000, null, 10, 2, 50, 'NC18K-001', '1234567890127', 4.2, '20x6x2mm', 'Vàng trắng 18K', 'Trắng', 'JewelryStore', 'Việt Nam', 24, true, false, true, false, 178, 9, 'Nhẫn cưới vàng trắng 18K - JewelryStore', 'Nhẫn cưới vàng trắng 18K với thiết kế tối giản', 'nhẫn cưới, nhẫn vàng trắng, nhẫn 18k', 'https://images.unsplash.com/photo-1603561596112-db0a0b8a0b8a?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 12 DAY, NOW()
FROM categories c WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-cuoi-vang-trang-18k');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Dây chuyền ngọc trai', 'day-chuyen-ngoc-trai', 'Dây chuyền ngọc trai tự nhiên với thiết kế cổ điển. Vẻ đẹp tinh tế và thanh lịch cho phụ nữ hiện đại.', 'Dây chuyền ngọc trai tự nhiên cổ điển', 25000000, 30000000, 25000000, 4, 1, 25, 'DCNT-001', '1234567890128', 15.8, '42cm', 'Ngọc trai', 'Trắng', 'JewelryStore', 'Việt Nam', 18, true, true, false, true, 312, 7, 'Dây chuyền ngọc trai - JewelryStore', 'Dây chuyền ngọc trai tự nhiên với thiết kế cổ điển', 'dây chuyền ngọc trai, trang sức ngọc trai, dây chuyền cao cấp', 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 10 DAY, NOW()
FROM categories c WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-ngoc-trai');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Nhẫn nam vàng 18K', 'nhan-nam-vang-18k', 'Nhẫn nam vàng 18K thiết kế nam tính, phù hợp cho các quý ông hiện đại.', 'Nhẫn nam vàng 18K thiết kế nam tính', 11000000, 11000000, null, 18, 3, 80, 'NN18K-001', '1234567890129', 6.8, '22x10x4mm', 'Vàng 18K', 'Vàng', 'JewelryStore', 'Việt Nam', 24, true, false, true, false, 98, 6, 'Nhẫn nam vàng 18K - JewelryStore', 'Nhẫn nam vàng 18K thiết kế nam tính', 'nhẫn nam, nhẫn vàng nam, nhẫn 18k nam', 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 8 DAY, NOW()
FROM categories c WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-nam-vang-18k');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Bông tai ngọc trai', 'bong-tai-ngoc-trai', 'Bông tai ngọc trai tự nhiên, thiết kế thanh lịch cho phụ nữ.', 'Bông tai ngọc trai tự nhiên thanh lịch', 16000000, 20000000, 16000000, 7, 2, 40, 'BTNT-001', '1234567890130', 4.5, '12x8mm', 'Ngọc trai', 'Trắng', 'JewelryStore', 'Việt Nam', 18, true, false, false, true, 167, 11, 'Bông tai ngọc trai - JewelryStore', 'Bông tai ngọc trai tự nhiên, thiết kế thanh lịch', 'bông tai ngọc trai, trang sức ngọc trai, bông tai cao cấp', 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 6 DAY, NOW()
FROM categories c WHERE c.slug = 'bong-tai'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'bong-tai-ngoc-trai');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Vòng tay charm vàng', 'vong-tay-charm-vang', 'Vòng tay charm vàng 18K với những charm đáng yêu, tạo phong cách cá tính.', 'Vòng tay charm vàng 18K cá tính', 8500000, 10000000, 8500000, 12, 3, 60, 'VT18K-001', '1234567890131', 9.2, '19cm', 'Vàng 18K', 'Vàng', 'JewelryStore', 'Việt Nam', 18, true, true, true, false, 201, 13, 'Vòng tay charm vàng - JewelryStore', 'Vòng tay charm vàng 18K với những charm đáng yêu', 'vòng tay charm, vòng tay vàng, trang sức charm', 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 4 DAY, NOW()
FROM categories c WHERE c.slug = 'vong-tay'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'vong-tay-charm-vang');

INSERT INTO products (name, slug, description, short_description, price, original_price, sale_price, stock, min_stock, max_stock, sku, barcode, weight, dimensions, material, color, brand, origin, warranty_period, is_active, is_featured, is_new, is_bestseller, view_count, sold_count, meta_title, meta_description, meta_keywords, thumbnail, category_id, created_at, updated_at)
SELECT 'Dây chuyền vàng 22K', 'day-chuyen-vang-22k', 'Dây chuyền vàng 22K cao cấp, thiết kế sang trọng.', 'Dây chuyền vàng 22K cao cấp', 22000000, 22000000, null, 5, 1, 30, 'DC22K-001', '1234567890132', 11.5, '40cm', 'Vàng 22K', 'Vàng', 'JewelryStore', 'Việt Nam', 24, true, false, false, true, 134, 4, 'Dây chuyền vàng 22K - JewelryStore', 'Dây chuyền vàng 22K cao cấp, thiết kế sang trọng', 'dây chuyền vàng, dây chuyền 22k, trang sức vàng cao cấp', 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id, NOW() - INTERVAL 2 DAY, NOW()
FROM categories c WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-vang-22k');

-- 6. Thêm product images
INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order, created_at, updated_at)
SELECT p.id, 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', 'Nhẫn vàng 24K cao cấp', true, 1, NOW() - INTERVAL 25 DAY, NOW()
FROM products p WHERE p.slug = 'nhan-vang-24k-cao-cap'
AND NOT EXISTS (SELECT 1 FROM product_images pi WHERE pi.product_id = p.id);

INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order, created_at, updated_at)
SELECT p.id, 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', 'Dây chuyền bạc 925', true, 1, NOW() - INTERVAL 20 DAY, NOW()
FROM products p WHERE p.slug = 'day-chuyen-bac-925'
AND NOT EXISTS (SELECT 1 FROM product_images pi WHERE pi.product_id = p.id);

INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order, created_at, updated_at)
SELECT p.id, 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', 'Lắc tay vàng hồng 18K', true, 1, NOW() - INTERVAL 18 DAY, NOW()
FROM products p WHERE p.slug = 'lac-tay-vang-hong-18k'
AND NOT EXISTS (SELECT 1 FROM product_images pi WHERE pi.product_id = p.id);

INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order, created_at, updated_at)
SELECT p.id, 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', 'Bông tai kim cương', true, 1, NOW() - INTERVAL 15 DAY, NOW()
FROM products p WHERE p.slug = 'bong-tai-kim-cuong'
AND NOT EXISTS (SELECT 1 FROM product_images pi WHERE pi.product_id = p.id);

-- 7. Thêm product sizes
INSERT INTO product_sizes (product_id, size, stock, is_active)
SELECT p.id, '16', 3, true FROM products p WHERE p.slug = 'nhan-vang-24k-cao-cap'
AND NOT EXISTS (SELECT 1 FROM product_sizes ps WHERE ps.product_id = p.id AND ps.size = '16');

INSERT INTO product_sizes (product_id, size, stock, is_active)
SELECT p.id, '17', 2, true FROM products p WHERE p.slug = 'nhan-vang-24k-cao-cap'
AND NOT EXISTS (SELECT 1 FROM product_sizes ps WHERE ps.product_id = p.id AND ps.size = '17');

INSERT INTO product_sizes (product_id, size, stock, is_active)
SELECT p.id, '18', 3, true FROM products p WHERE p.slug = 'nhan-vang-24k-cao-cap'
AND NOT EXISTS (SELECT 1 FROM product_sizes ps WHERE ps.product_id = p.id AND ps.size = '18');

-- 8. Thêm orders
INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address, shipping_phone, shipping_name, customer_name, customer_phone, customer_email, payment_method, payment_status, subtotal, shipping_fee, discount_amount, created_at, updated_at, paid_at)
SELECT u.id, 'ORD-001', 'DELIVERED', 15000000, '123 Đường ABC, Quận 1, TP.HCM', '0987654321', 'Nguyễn Văn An', 'Nguyễn Văn An', '0987654321', 'customer1@gmail.com', 'PAYOS', 'SUCCESS', 15000000, 0, 0, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 20 DAY
FROM users u WHERE u.email = 'customer1@gmail.com'
AND NOT EXISTS (SELECT 1 FROM orders o WHERE o.order_number = 'ORD-001');

INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address, shipping_phone, shipping_name, customer_name, customer_phone, customer_email, payment_method, payment_status, subtotal, shipping_fee, discount_amount, created_at, updated_at, paid_at)
SELECT u.id, 'ORD-002', 'DELIVERED', 3500000, '456 Đường DEF, Quận 2, TP.HCM', '0987654322', 'Trần Thị Bình', 'Trần Thị Bình', '0987654322', 'customer2@gmail.com', 'PAYOS', 'SUCCESS', 3500000, 0, 0, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 15 DAY
FROM users u WHERE u.email = 'customer2@gmail.com'
AND NOT EXISTS (SELECT 1 FROM orders o WHERE o.order_number = 'ORD-002');

INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address, shipping_phone, shipping_name, customer_name, customer_phone, customer_email, payment_method, payment_status, subtotal, shipping_fee, discount_amount, created_at, updated_at, paid_at)
SELECT u.id, 'ORD-003', 'PROCESSING', 12000000, '789 Đường GHI, Quận 3, TP.HCM', '0987654323', 'Lê Văn Cường', 'Lê Văn Cường', '0987654323', 'customer3@gmail.com', 'PAYOS', 'SUCCESS', 12000000, 0, 0, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 10 DAY
FROM users u WHERE u.email = 'customer3@gmail.com'
AND NOT EXISTS (SELECT 1 FROM orders o WHERE o.order_number = 'ORD-003');

INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address, shipping_phone, shipping_name, customer_name, customer_phone, customer_email, payment_method, payment_status, subtotal, shipping_fee, discount_amount, created_at, updated_at, paid_at)
SELECT u.id, 'ORD-004', 'PENDING', 35000000, '321 Đường JKL, Quận 4, TP.HCM', '0987654324', 'Phạm Thị Dung', 'Phạm Thị Dung', '0987654324', 'customer4@gmail.com', 'PAYOS', 'PENDING', 35000000, 0, 0, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY, null
FROM users u WHERE u.email = 'customer4@gmail.com'
AND NOT EXISTS (SELECT 1 FROM orders o WHERE o.order_number = 'ORD-004');

INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address, shipping_phone, shipping_name, customer_name, customer_phone, customer_email, payment_method, payment_status, subtotal, shipping_fee, discount_amount, created_at, updated_at, paid_at)
SELECT u.id, 'ORD-005', 'DELIVERED', 18000000, '654 Đường MNO, Quận 5, TP.HCM', '0987654325', 'Hoàng Văn Em', 'Hoàng Văn Em', '0987654325', 'customer5@gmail.com', 'PAYOS', 'SUCCESS', 18000000, 0, 0, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 3 DAY
FROM users u WHERE u.email = 'customer5@gmail.com'
AND NOT EXISTS (SELECT 1 FROM orders o WHERE o.order_number = 'ORD-005');

-- 9. Thêm order items
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal, size_value, color_value, product_name, product_sku, price, discount_amount, final_price, created_at)
SELECT o.id, p.id, 1, 15000000, 15000000, '17', 'Vàng', 'Nhẫn vàng 24K cao cấp', 'N24K-001', 15000000, 0, 15000000, NOW() - INTERVAL 20 DAY
FROM orders o, products p 
WHERE o.order_number = 'ORD-001' AND p.slug = 'nhan-vang-24k-cao-cap'
AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal, size_value, color_value, product_name, product_sku, price, discount_amount, final_price, created_at)
SELECT o.id, p.id, 1, 3500000, 3500000, null, 'Bạc', 'Dây chuyền bạc 925', 'DC925-001', 3500000, 0, 3500000, NOW() - INTERVAL 15 DAY
FROM orders o, products p 
WHERE o.order_number = 'ORD-002' AND p.slug = 'day-chuyen-bac-925'
AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal, size_value, color_value, product_name, product_sku, price, discount_amount, final_price, created_at)
SELECT o.id, p.id, 1, 12000000, 12000000, '18cm', 'Vàng hồng', 'Lắc tay vàng hồng 18K', 'LT18K-001', 12000000, 0, 12000000, NOW() - INTERVAL 10 DAY
FROM orders o, products p 
WHERE o.order_number = 'ORD-003' AND p.slug = 'lac-tay-vang-hong-18k'
AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal, size_value, color_value, product_name, product_sku, price, discount_amount, final_price, created_at)
SELECT o.id, p.id, 1, 35000000, 35000000, '15x8mm', 'Trắng', 'Bông tai kim cương', 'BTKC-001', 35000000, 0, 35000000, NOW() - INTERVAL 5 DAY
FROM orders o, products p 
WHERE o.order_number = 'ORD-004' AND p.slug = 'bong-tai-kim-cuong'
AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal, size_value, color_value, product_name, product_sku, price, discount_amount, final_price, created_at)
SELECT o.id, p.id, 1, 18000000, 18000000, '20x6x2mm', 'Trắng', 'Nhẫn cưới vàng trắng 18K', 'NC18K-001', 18000000, 0, 18000000, NOW() - INTERVAL 3 DAY
FROM orders o, products p 
WHERE o.order_number = 'ORD-005' AND p.slug = 'nhan-cuoi-vang-trang-18k'
AND NOT EXISTS (SELECT 1 FROM order_items oi WHERE oi.order_id = o.id);

-- 10. Thêm payments
INSERT INTO payments (order_id, payment_method, amount, status, transaction_id, payos_payment_id, payos_payment_url, payos_qr_code, payos_checksum, description, created_at, updated_at, paid_at)
SELECT o.id, 'PAYOS', 15000000, 'SUCCESS', 'TXN-001', 'PAYOS-001', 'https://payos.vn/pay/PAYOS-001', 'QR-001', 'CHECKSUM-001', 'Thanh toán đơn hàng ORD-001', NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 20 DAY
FROM orders o WHERE o.order_number = 'ORD-001'
AND NOT EXISTS (SELECT 1 FROM payments p WHERE p.order_id = o.id);

INSERT INTO payments (order_id, payment_method, amount, status, transaction_id, payos_payment_id, payos_payment_url, payos_qr_code, payos_checksum, description, created_at, updated_at, paid_at)
SELECT o.id, 'PAYOS', 3500000, 'SUCCESS', 'TXN-002', 'PAYOS-002', 'https://payos.vn/pay/PAYOS-002', 'QR-002', 'CHECKSUM-002', 'Thanh toán đơn hàng ORD-002', NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 15 DAY
FROM orders o WHERE o.order_number = 'ORD-002'
AND NOT EXISTS (SELECT 1 FROM payments p WHERE p.order_id = o.id);

INSERT INTO payments (order_id, payment_method, amount, status, transaction_id, payos_payment_id, payos_payment_url, payos_qr_code, payos_checksum, description, created_at, updated_at, paid_at)
SELECT o.id, 'PAYOS', 12000000, 'SUCCESS', 'TXN-003', 'PAYOS-003', 'https://payos.vn/pay/PAYOS-003', 'QR-003', 'CHECKSUM-003', 'Thanh toán đơn hàng ORD-003', NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY
FROM orders o WHERE o.order_number = 'ORD-003'
AND NOT EXISTS (SELECT 1 FROM payments p WHERE p.order_id = o.id);

INSERT INTO payments (order_id, payment_method, amount, status, transaction_id, payos_payment_id, payos_payment_url, payos_qr_code, payos_checksum, description, created_at, updated_at, paid_at)
SELECT o.id, 'PAYOS', 35000000, 'PENDING', 'TXN-004', 'PAYOS-004', 'https://payos.vn/pay/PAYOS-004', 'QR-004', 'CHECKSUM-004', 'Thanh toán đơn hàng ORD-004', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY, null
FROM orders o WHERE o.order_number = 'ORD-004'
AND NOT EXISTS (SELECT 1 FROM payments p WHERE p.order_id = o.id);

INSERT INTO payments (order_id, payment_method, amount, status, transaction_id, payos_payment_id, payos_payment_url, payos_qr_code, payos_checksum, description, created_at, updated_at, paid_at)
SELECT o.id, 'PAYOS', 18000000, 'SUCCESS', 'TXN-005', 'PAYOS-005', 'https://payos.vn/pay/PAYOS-005', 'QR-005', 'CHECKSUM-005', 'Thanh toán đơn hàng ORD-005', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY
FROM orders o WHERE o.order_number = 'ORD-005'
AND NOT EXISTS (SELECT 1 FROM payments p WHERE p.order_id = o.id);

-- 11. Thêm reviews
INSERT INTO reviews (user_id, product_id, rating, comment, is_verified_purchase, helpful_count, is_approved, created_at, updated_at)
SELECT u.id, p.id, 5, 'Sản phẩm rất đẹp, chất lượng cao. Tôi rất hài lòng với sản phẩm này!', true, 3, true, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 18 DAY
FROM users u, products p 
WHERE u.email = 'customer1@gmail.com' AND p.slug = 'nhan-vang-24k-cao-cap'
AND NOT EXISTS (SELECT 1 FROM reviews r WHERE r.user_id = u.id AND r.product_id = p.id);

INSERT INTO reviews (user_id, product_id, rating, comment, is_verified_purchase, helpful_count, is_approved, created_at, updated_at)
SELECT u.id, p.id, 4, 'Dây chuyền đẹp, thiết kế tinh tế. Phù hợp với mọi trang phục.', true, 2, true, NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 13 DAY
FROM users u, products p 
WHERE u.email = 'customer2@gmail.com' AND p.slug = 'day-chuyen-bac-925'
AND NOT EXISTS (SELECT 1 FROM reviews r WHERE r.user_id = u.id AND r.product_id = p.id);

INSERT INTO reviews (user_id, product_id, rating, comment, is_verified_purchase, helpful_count, is_approved, created_at, updated_at)
SELECT u.id, p.id, 5, 'Lắc tay rất sang trọng, chất lượng tuyệt vời. Đáng giá từng đồng!', true, 5, true, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY
FROM users u, products p 
WHERE u.email = 'customer3@gmail.com' AND p.slug = 'lac-tay-vang-hong-18k'
AND NOT EXISTS (SELECT 1 FROM reviews r WHERE r.user_id = u.id AND r.product_id = p.id);

INSERT INTO reviews (user_id, product_id, rating, comment, is_verified_purchase, helpful_count, is_approved, created_at, updated_at)
SELECT u.id, p.id, 5, 'Bông tai kim cương tuyệt đẹp, ánh sáng lấp lánh. Sản phẩm cao cấp!', true, 4, true, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY
FROM users u, products p 
WHERE u.email = 'customer5@gmail.com' AND p.slug = 'bong-tai-kim-cuong'
AND NOT EXISTS (SELECT 1 FROM reviews r WHERE r.user_id = u.id AND r.product_id = p.id);

-- 12. Thêm promotions
INSERT INTO promotions (name, description, type, value, min_order_amount, code, start_date, end_date, active, usage_limit, used_count, applicable_products, applicable_categories, applicable_product_ids, created_at, updated_at)
SELECT 'Giảm giá 10% cho đơn hàng trên 10 triệu', 'Áp dụng cho tất cả sản phẩm, giảm 10% cho đơn hàng từ 10 triệu đồng', 'PERCENTAGE', 10.00, 10000000, 'SAVE10', NOW() - INTERVAL 30 DAY, NOW() + INTERVAL 30 DAY, true, 100, 5, 'ALL', null, null, NOW() - INTERVAL 30 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM promotions WHERE code = 'SAVE10');

INSERT INTO promotions (name, description, type, value, min_order_amount, code, start_date, end_date, active, usage_limit, used_count, applicable_products, applicable_categories, applicable_product_ids, created_at, updated_at)
SELECT 'Miễn phí ship cho đơn hàng trên 5 triệu', 'Miễn phí vận chuyển cho đơn hàng từ 5 triệu đồng', 'FREE_SHIPPING', 0.00, 5000000, 'FREESHIP', NOW() - INTERVAL 20 DAY, NOW() + INTERVAL 20 DAY, true, 50, 8, 'ALL', null, null, NOW() - INTERVAL 20 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM promotions WHERE code = 'FREESHIP');

INSERT INTO promotions (name, description, type, value, min_order_amount, code, start_date, end_date, active, usage_limit, used_count, applicable_products, applicable_categories, applicable_product_ids, created_at, updated_at)
SELECT 'Giảm 500k cho đơn hàng trên 20 triệu', 'Giảm trực tiếp 500,000 đồng cho đơn hàng từ 20 triệu đồng', 'FIXED_AMOUNT', 500000.00, 20000000, 'SAVE500K', NOW() - INTERVAL 15 DAY, NOW() + INTERVAL 15 DAY, true, 20, 2, 'ALL', null, null, NOW() - INTERVAL 15 DAY, NOW()
WHERE NOT EXISTS (SELECT 1 FROM promotions WHERE code = 'SAVE500K');

-- 13. Thêm customer addresses
INSERT INTO customer_addresses (user_id, type, full_name, phone, address, city, province, postal_code, is_default, created_at, updated_at)
SELECT u.id, 'HOME', 'Nguyễn Văn An', '0987654321', '123 Đường ABC, Phường 1', 'Quận 1', 'TP.HCM', '700000', true, NOW() - INTERVAL 25 DAY, NOW()
FROM users u WHERE u.email = 'customer1@gmail.com'
AND NOT EXISTS (SELECT 1 FROM customer_addresses ca WHERE ca.user_id = u.id);

INSERT INTO customer_addresses (user_id, type, full_name, phone, address, city, province, postal_code, is_default, created_at, updated_at)
SELECT u.id, 'HOME', 'Trần Thị Bình', '0987654322', '456 Đường DEF, Phường 2', 'Quận 2', 'TP.HCM', '700000', true, NOW() - INTERVAL 20 DAY, NOW()
FROM users u WHERE u.email = 'customer2@gmail.com'
AND NOT EXISTS (SELECT 1 FROM customer_addresses ca WHERE ca.user_id = u.id);

INSERT INTO customer_addresses (user_id, type, full_name, phone, address, city, province, postal_code, is_default, created_at, updated_at)
SELECT u.id, 'HOME', 'Lê Văn Cường', '0987654323', '789 Đường GHI, Phường 3', 'Quận 3', 'TP.HCM', '700000', true, NOW() - INTERVAL 15 DAY, NOW()
FROM users u WHERE u.email = 'customer3@gmail.com'
AND NOT EXISTS (SELECT 1 FROM customer_addresses ca WHERE ca.user_id = u.id);

INSERT INTO customer_addresses (user_id, type, full_name, phone, address, city, province, postal_code, is_default, created_at, updated_at)
SELECT u.id, 'HOME', 'Phạm Thị Dung', '0987654324', '321 Đường JKL, Phường 4', 'Quận 4', 'TP.HCM', '700000', true, NOW() - INTERVAL 10 DAY, NOW()
FROM users u WHERE u.email = 'customer4@gmail.com'
AND NOT EXISTS (SELECT 1 FROM customer_addresses ca WHERE ca.user_id = u.id);

INSERT INTO customer_addresses (user_id, type, full_name, phone, address, city, province, postal_code, is_default, created_at, updated_at)
SELECT u.id, 'HOME', 'Hoàng Văn Em', '0987654325', '654 Đường MNO, Phường 5', 'Quận 5', 'TP.HCM', '700000', true, NOW() - INTERVAL 5 DAY, NOW()
FROM users u WHERE u.email = 'customer5@gmail.com'
AND NOT EXISTS (SELECT 1 FROM customer_addresses ca WHERE ca.user_id = u.id);

-- 14. Thêm wishlist
INSERT INTO wishlist (user_id, product_id, is_notified, created_at, updated_at)
SELECT u.id, p.id, false, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY
FROM users u, products p 
WHERE u.email = 'customer1@gmail.com' AND p.slug = 'bong-tai-kim-cuong'
AND NOT EXISTS (SELECT 1 FROM wishlist w WHERE w.user_id = u.id AND w.product_id = p.id);

INSERT INTO wishlist (user_id, product_id, is_notified, created_at, updated_at)
SELECT u.id, p.id, false, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY
FROM users u, products p 
WHERE u.email = 'customer2@gmail.com' AND p.slug = 'day-chuyen-ngoc-trai'
AND NOT EXISTS (SELECT 1 FROM wishlist w WHERE w.user_id = u.id AND w.product_id = p.id);

INSERT INTO wishlist (user_id, product_id, is_notified, created_at, updated_at)
SELECT u.id, p.id, false, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY
FROM users u, products p 
WHERE u.email = 'customer3@gmail.com' AND p.slug = 'vong-tay-charm-vang'
AND NOT EXISTS (SELECT 1 FROM wishlist w WHERE w.user_id = u.id AND w.product_id = p.id);

INSERT INTO wishlist (user_id, product_id, is_notified, created_at, updated_at)
SELECT u.id, p.id, false, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY
FROM users u, products p 
WHERE u.email = 'customer4@gmail.com' AND p.slug = 'nhan-cuoi-vang-trang-18k'
AND NOT EXISTS (SELECT 1 FROM wishlist w WHERE w.user_id = u.id AND w.product_id = p.id);

INSERT INTO wishlist (user_id, product_id, is_notified, created_at, updated_at)
SELECT u.id, p.id, false, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY
FROM users u, products p 
WHERE u.email = 'customer5@gmail.com' AND p.slug = 'nhan-vang-24k-cao-cap'
AND NOT EXISTS (SELECT 1 FROM wishlist w WHERE w.user_id = u.id AND w.product_id = p.id);

-- 15. Thêm notifications
INSERT INTO notifications (user_id, title, message, type, status, is_read, read_at, created_at, updated_at, expires_at, action_url, metadata)
SELECT u.id, 'Đơn hàng đã được giao', 'Đơn hàng ORD-001 của bạn đã được giao thành công. Cảm ơn bạn đã mua sắm!', 'ORDER_STATUS', 'READ', true, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 18 DAY, NOW() + INTERVAL 30 DAY, '/order-history', '{"order_id": 1, "order_number": "ORD-001"}'
FROM users u WHERE u.email = 'customer1@gmail.com'
AND NOT EXISTS (SELECT 1 FROM notifications n WHERE n.user_id = u.id AND n.title = 'Đơn hàng đã được giao');

INSERT INTO notifications (user_id, title, message, type, status, is_read, read_at, created_at, updated_at, expires_at, action_url, metadata)
SELECT u.id, 'Đơn hàng đã được giao', 'Đơn hàng ORD-002 của bạn đã được giao thành công. Cảm ơn bạn đã mua sắm!', 'ORDER_STATUS', 'READ', true, NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 13 DAY, NOW() + INTERVAL 30 DAY, '/order-history', '{"order_id": 2, "order_number": "ORD-002"}'
FROM users u WHERE u.email = 'customer2@gmail.com'
AND NOT EXISTS (SELECT 1 FROM notifications n WHERE n.user_id = u.id AND n.title = 'Đơn hàng đã được giao');

INSERT INTO notifications (user_id, title, message, type, status, is_read, read_at, created_at, updated_at, expires_at, action_url, metadata)
SELECT u.id, 'Đơn hàng đang được xử lý', 'Đơn hàng ORD-003 của bạn đang được xử lý. Chúng tôi sẽ thông báo khi đơn hàng được giao.', 'ORDER_STATUS', 'UNREAD', false, null, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY, NOW() + INTERVAL 30 DAY, '/order-history', '{"order_id": 3, "order_number": "ORD-003"}'
FROM users u WHERE u.email = 'customer3@gmail.com'
AND NOT EXISTS (SELECT 1 FROM notifications n WHERE n.user_id = u.id AND n.title = 'Đơn hàng đang được xử lý');

INSERT INTO notifications (user_id, title, message, type, status, is_read, read_at, created_at, updated_at, expires_at, action_url, metadata)
SELECT u.id, 'Khuyến mãi mới', 'Giảm giá 10% cho đơn hàng trên 10 triệu. Sử dụng mã SAVE10 ngay hôm nay!', 'PROMOTION', 'UNREAD', false, null, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY, NOW() + INTERVAL 25 DAY, '/products', '{"promotion_code": "SAVE10", "discount": "10%"}'
FROM users u WHERE u.email = 'customer4@gmail.com'
AND NOT EXISTS (SELECT 1 FROM notifications n WHERE n.user_id = u.id AND n.title = 'Khuyến mãi mới');

INSERT INTO notifications (user_id, title, message, type, status, is_read, read_at, created_at, updated_at, expires_at, action_url, metadata)
SELECT u.id, 'Đánh giá sản phẩm', 'Cảm ơn bạn đã mua sản phẩm! Hãy đánh giá sản phẩm để giúp chúng tôi cải thiện dịch vụ.', 'REVIEW_APPROVED', 'UNREAD', false, null, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY, NOW() + INTERVAL 30 DAY, '/product-detail/5', '{"product_id": 5, "product_name": "Nhẫn cưới vàng trắng 18K"}'
FROM users u WHERE u.email = 'customer5@gmail.com'
AND NOT EXISTS (SELECT 1 FROM notifications n WHERE n.user_id = u.id AND n.title = 'Đánh giá sản phẩm');

-- 16. Thêm shipping
INSERT INTO shipping (order_id, shipping_method, tracking_number, status, from_address, to_address, customer_name, customer_phone, shipping_fee, estimated_delivery, actual_delivery, created_at, updated_at, notes)
SELECT o.id, 'GHTK', 'GHTK-001', 'DELIVERED', '123 Đường ABC, Quận 1, TP.HCM', '123 Đường ABC, Quận 1, TP.HCM', 'Nguyễn Văn An', '0987654321', 0, NOW() - INTERVAL 19 DAY, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 18 DAY, 'Giao hàng thành công'
FROM orders o WHERE o.order_number = 'ORD-001'
AND NOT EXISTS (SELECT 1 FROM shipping s WHERE s.order_id = o.id);

INSERT INTO shipping (order_id, shipping_method, tracking_number, status, from_address, to_address, customer_name, customer_phone, shipping_fee, estimated_delivery, actual_delivery, created_at, updated_at, notes)
SELECT o.id, 'GHN', 'GHN-002', 'DELIVERED', '456 Đường DEF, Quận 2, TP.HCM', '456 Đường DEF, Quận 2, TP.HCM', 'Trần Thị Bình', '0987654322', 0, NOW() - INTERVAL 14 DAY, NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 13 DAY, 'Giao hàng thành công'
FROM orders o WHERE o.order_number = 'ORD-002'
AND NOT EXISTS (SELECT 1 FROM shipping s WHERE s.order_id = o.id);

INSERT INTO shipping (order_id, shipping_method, tracking_number, status, from_address, to_address, customer_name, customer_phone, shipping_fee, estimated_delivery, actual_delivery, created_at, updated_at, notes)
SELECT o.id, 'VIETTEL_POST', 'VT-003', 'IN_TRANSIT', '789 Đường GHI, Quận 3, TP.HCM', '789 Đường GHI, Quận 3, TP.HCM', 'Lê Văn Cường', '0987654323', 0, NOW() + INTERVAL 2 DAY, null, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 8 DAY, 'Đang vận chuyển'
FROM orders o WHERE o.order_number = 'ORD-003'
AND NOT EXISTS (SELECT 1 FROM shipping s WHERE s.order_id = o.id);

INSERT INTO shipping (order_id, shipping_method, tracking_number, status, from_address, to_address, customer_name, customer_phone, shipping_fee, estimated_delivery, actual_delivery, created_at, updated_at, notes)
SELECT o.id, 'J_T', 'JT-004', 'PENDING', '321 Đường JKL, Quận 4, TP.HCM', '321 Đường JKL, Quận 4, TP.HCM', 'Phạm Thị Dung', '0987654324', 0, NOW() + INTERVAL 5 DAY, null, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY, 'Chờ xử lý'
FROM orders o WHERE o.order_number = 'ORD-004'
AND NOT EXISTS (SELECT 1 FROM shipping s WHERE s.order_id = o.id);

INSERT INTO shipping (order_id, shipping_method, tracking_number, status, from_address, to_address, customer_name, customer_phone, shipping_fee, estimated_delivery, actual_delivery, created_at, updated_at, notes)
SELECT o.id, 'NINJA_VAN', 'NV-005', 'DELIVERED', '654 Đường MNO, Quận 5, TP.HCM', '654 Đường MNO, Quận 5, TP.HCM', 'Hoàng Văn Em', '0987654325', 0, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 1 DAY, 'Giao hàng thành công'
FROM orders o WHERE o.order_number = 'ORD-005'
AND NOT EXISTS (SELECT 1 FROM shipping s WHERE s.order_id = o.id);
