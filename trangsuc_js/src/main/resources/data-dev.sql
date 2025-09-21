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

-- 2. Thêm user admin
INSERT INTO users (email, password, full_name, phone_number, address, enabled, created_at, updated_at)
SELECT 'admin@gmail.com', '$2a$10$cgziXouzLTHpKg2VcRYIkuwS/lePp/PxXYi6ecWKzTXHZi2eg86ve', 'Administrator', '0123456789', 'Admin Address', true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');

-- 3. Gán role ADMIN cho user admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@gmail.com' 
AND r.name = 'ROLE_ADMIN'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

-- 4. Thêm danh mục sản phẩm
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

-- 5. Thêm sản phẩm mẫu với các trường featured, new, bestseller
INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Nhan vang 24K', 'nhan-vang-24k', 'Nhan vang 24K cao cap, thiet ke tinh te voi hoa van co dien. San pham duoc che tac thu cong boi nhung nghe nhan lanh nghe nhat.', 10000000, 12000000, 10000000, 10, 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', c.id, true, true, false, true, 'JewelryStore', 'Vang 24K', 'Vang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-vang-24k');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Day chuyen bac 925', 'day-chuyen-bac-925', 'Day chuyen bac 925 cao cap, thiet ke hien dai voi mat day tinh te. Phu hop cho moi dip dac biet.', 2500000, 3000000, 2500000, 15, 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id, true, false, true, false, 'JewelryStore', 'Bac 925', 'Bac', NOW(), NOW()
FROM categories c
WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-bac-925');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Lac tay vang hong', 'lac-tay-vang-hong', 'Lac tay vang hong 18K, thiet ke sang trong voi nhung vien da quy nho xinh. Tao diem nhan hoan hao cho co tay.', 8500000, 8500000, null, 8, 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', c.id, true, true, false, true, 'JewelryStore', 'Vang 18K', 'Vang hong', NOW(), NOW()
FROM categories c
WHERE c.slug = 'lac-tay'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'lac-tay-vang-hong');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Bong tai kim cuong', 'bong-tai-kim-cuong', 'Bong tai kim cuong tu nhien, thiet ke doc dao voi nhung vien kim cuong duoc cat tia hoan hao. San pham cao cap nhat cua chung toi.', 25000000, 30000000, 25000000, 5, 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', c.id, true, true, false, true, 'JewelryStore', 'Kim cuong', 'Trang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'bong-tai'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'bong-tai-kim-cuong');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Nhan cuoi vang trang', 'nhan-cuoi-vang-trang', 'Nhan cuoi vang trang 18K voi thiet ke toi gian nhung sang trong. Bieu tuong cua tinh yeu vinh cuu.', 12000000, 12000000, null, 12, 'https://images.unsplash.com/photo-1603561596112-db0a0b8a0b8a?w=400&h=400&fit=crop', c.id, true, false, true, false, 'JewelryStore', 'Vang trang 18K', 'Trang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-cuoi-vang-trang');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Day chuyen ngoc trai', 'day-chuyen-ngoc-trai', 'Day chuyen ngoc trai tu nhien voi thiet ke co dien. Ve dep tinh te va thanh lich cho phu nu hien dai.', 18000000, 20000000, 18000000, 6, 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id, true, true, false, true, 'JewelryStore', 'Ngoc trai', 'Trang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-ngoc-trai');

-- Them them san pham de co du data cho cac section
INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Nhan nam vang 18K', 'nhan-nam-vang-18k', 'Nhan nam vang 18K thiet ke nam tinh, phu hop cho cac quy ong hien dai.', 8000000, 8000000, null, 20, 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', c.id, true, false, true, false, 'JewelryStore', 'Vang 18K', 'Vang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-nam-vang-18k');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Bong tai ngoc trai', 'bong-tai-ngoc-trai', 'Bong tai ngoc trai tu nhien, thiet ke thanh lich cho phu nu.', 12000000, 15000000, 12000000, 8, 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', c.id, true, false, false, true, 'JewelryStore', 'Ngoc trai', 'Trang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'bong-tai'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'bong-tai-ngoc-trai');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Vong tay charm vang', 'vong-tay-charm-vang', 'Vong tay charm vang 18K voi nhung charm dang yeu, tao phong cach ca tinh.', 6000000, 7000000, 6000000, 15, 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', c.id, true, true, true, false, 'JewelryStore', 'Vang 18K', 'Vang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'lac-tay'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'vong-tay-charm-vang');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Day chuyen vang 22K', 'day-chuyen-vang-22k', 'Day chuyen vang 22K cao cap, thiet ke sang trong.', 15000000, 15000000, null, 10, 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop', c.id, true, false, false, true, 'JewelryStore', 'Vang 22K', 'Vang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'day-chuyen'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'day-chuyen-vang-22k');

-- Them 3 san pham moi nua de co du 5 san pham
INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Nhan kim cuong nu', 'nhan-kim-cuong-nu', 'Nhan kim cuong danh cho nu gioi, thiet ke tinh te va sang trong.', 20000000, 20000000, null, 5, 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop', c.id, true, false, true, false, 'JewelryStore', 'Kim cuong', 'Trang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'nhan'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'nhan-kim-cuong-nu');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Lac chan bac 925', 'lac-chan-bac-925', 'Lac chan bac 925 thoi trang, phu hop cho cac ban tre.', 3500000, 3500000, null, 12, 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop', c.id, true, false, true, false, 'JewelryStore', 'Bac 925', 'Bac', NOW(), NOW()
FROM categories c
WHERE c.slug = 'lac-tay'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'lac-chan-bac-925');

INSERT INTO products (name, slug, description, price, original_price, sale_price, stock, thumbnail, category_id, is_active, is_featured, is_new, is_bestseller, brand, material, color, created_at, updated_at)
SELECT 'Bong tai vang trang', 'bong-tai-vang-trang', 'Bong tai vang trang 18K phong cach hien dai.', 9500000, 9500000, null, 8, 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop', c.id, true, false, true, false, 'JewelryStore', 'Vang trang 18K', 'Trang', NOW(), NOW()
FROM categories c
WHERE c.slug = 'bong-tai'
AND NOT EXISTS (SELECT 1 FROM products WHERE slug = 'bong-tai-vang-trang');
