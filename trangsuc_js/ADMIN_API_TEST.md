# Admin API Testing Guide

## Các API Admin đã được tạo

### 1. Product Management
- `POST /api/admin/products` - Tạo sản phẩm mới
- `GET /api/admin/products` - Lấy danh sách sản phẩm
- `PUT /api/admin/products/{id}` - Cập nhật sản phẩm
- `DELETE /api/admin/products/{id}` - Xóa sản phẩm
- `POST /api/admin/products/{id}/images` - Upload hình ảnh sản phẩm
- `DELETE /api/admin/products/{id}/images` - Xóa hình ảnh sản phẩm

### 1.1. Product Image Management (NEW)
- `GET /api/admin/products/{id}/images` - Lấy danh sách ảnh của sản phẩm
- `POST /api/admin/products/{id}/images` - Thêm ảnh mới cho sản phẩm
- `PUT /api/admin/products/images/{imageId}` - Cập nhật thông tin ảnh
- `DELETE /api/admin/products/images/{imageId}` - Xóa ảnh theo ID
- `PUT /api/admin/products/{id}/images/{imageId}/primary` - Đặt ảnh làm ảnh chính
- `PUT /api/admin/products/{id}/images/reorder` - Sắp xếp lại thứ tự ảnh

### 2. Category Management
- `POST /api/admin/categories` - Tạo danh mục mới
- `GET /api/admin/categories` - Lấy danh sách danh mục
- `PUT /api/admin/categories/{id}` - Cập nhật danh mục
- `DELETE /api/admin/categories/{id}` - Xóa danh mục

### 3. User Management
- `GET /api/admin/users` - Lấy danh sách người dùng (có phân trang)
- `GET /api/admin/users/{id}` - Lấy thông tin người dùng theo ID
- `PUT /api/admin/users/{id}/status` - Cập nhật trạng thái người dùng
- `PUT /api/admin/users/{id}/roles` - Cập nhật quyền người dùng
- `DELETE /api/admin/users/{id}` - Xóa người dùng

### 4. Order Management
- `GET /api/admin/orders` - Lấy danh sách đơn hàng (có phân trang)
- `GET /api/admin/orders/{id}` - Lấy chi tiết đơn hàng
- `PUT /api/admin/orders/{id}/status` - Cập nhật trạng thái đơn hàng
- `PUT /api/admin/orders/{id}/cancel` - Hủy đơn hàng
- `GET /api/admin/orders/status/{status}` - Lấy đơn hàng theo trạng thái

### 5. Inventory Management
- `GET /api/admin/inventory` - Lấy trạng thái kho hàng
- `GET /api/admin/inventory/low-stock` - Lấy sản phẩm sắp hết hàng
- `GET /api/admin/inventory/out-of-stock` - Lấy sản phẩm hết hàng
- `PUT /api/admin/inventory/{productId}/stock` - Cập nhật số lượng tồn kho
- `PUT /api/admin/inventory/{productId}/min-max-stock` - Thiết lập min/max stock

### 6. Promotion Management
- `POST /api/admin/promotions` - Tạo khuyến mãi mới
- `GET /api/admin/promotions` - Lấy danh sách khuyến mãi
- `PUT /api/admin/promotions/{id}` - Cập nhật khuyến mãi
- `DELETE /api/admin/promotions/{id}` - Xóa khuyến mãi
- `PUT /api/admin/promotions/{id}/toggle` - Bật/tắt khuyến mãi

### 7. Dashboard & Statistics
- `GET /api/admin/dashboard/stats` - Lấy thống kê dashboard
- `GET /api/admin/reports/revenue` - Lấy báo cáo doanh thu
- `GET /api/admin/reports/revenue/date-range` - Lấy báo cáo doanh thu theo khoảng thời gian

### 8. Export Reports
- `GET /api/admin/reports/orders/export` - Xuất báo cáo đơn hàng ra Excel
- `GET /api/admin/reports/products/export` - Xuất báo cáo sản phẩm ra Excel
- `GET /api/admin/reports/customers/export` - Xuất báo cáo khách hàng ra Excel

### 9. Shipping Management
- `GET /api/admin/shipping` - Lấy danh sách vận chuyển (có phân trang)
- `GET /api/admin/shipping/{id}` - Lấy thông tin vận chuyển theo ID
- `GET /api/admin/shipping/order/{orderId}` - Lấy thông tin vận chuyển theo đơn hàng
- `POST /api/admin/shipping` - Tạo thông tin vận chuyển mới
- `PUT /api/admin/shipping/{id}/status` - Cập nhật trạng thái vận chuyển
- `PUT /api/admin/shipping/{id}/tracking` - Cập nhật mã tracking
- `GET /api/admin/shipping/status/{status}` - Lấy vận chuyển theo trạng thái
- `GET /api/admin/shipping/method/{method}` - Lấy vận chuyển theo phương thức

## Cách test API

### 1. Sử dụng Postman hoặc curl

```bash
# Test tạo sản phẩm mới với các trường mới
curl -X POST http://localhost:8080/api/admin/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Nhẫn vàng 18k",
    "slug": "nhan-vang-18k",
    "description": "Nhẫn vàng 18k đẹp, thiết kế tinh xảo",
    "shortDescription": "Nhẫn vàng 18k cao cấp",
    "price": 5000000,
    "originalPrice": 6000000,
    "salePrice": 5000000,
    "stock": 10,
    "minStock": 2,
    "maxStock": 50,
    "sku": "RING-18K-001",
    "barcode": "1234567890123",
    "weight": 5.5,
    "dimensions": "2cm x 2cm x 1cm",
    "material": "Vàng 18k",
    "color": "Vàng",
    "brand": "JewelryStore",
    "origin": "Việt Nam",
    "warrantyPeriod": 12,
    "isActive": true,
    "isFeatured": true,
    "isNew": true,
    "isBestseller": false,
    "metaTitle": "Nhẫn vàng 18k - JewelryStore",
    "metaDescription": "Nhẫn vàng 18k cao cấp, thiết kế tinh xảo",
    "metaKeywords": "nhẫn, vàng, 18k, trang sức",
    "thumbnail": "https://example.com/thumbnail.jpg",
    "categoryId": 1
  }'

# Test lấy danh sách sản phẩm
curl -X GET http://localhost:8080/api/admin/products \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Test lấy danh sách ảnh của sản phẩm
curl -X GET http://localhost:8080/api/admin/products/1/images \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Test thêm ảnh mới cho sản phẩm
curl -X POST http://localhost:8080/api/admin/products/1/images \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "imageUrl=https://example.com/product-image-1.jpg&altText=Ảnh sản phẩm chính"

# Test cập nhật thông tin ảnh
curl -X PUT http://localhost:8080/api/admin/products/images/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d "altText=Ảnh sản phẩm đã cập nhật&sortOrder=0"

# Test đặt ảnh làm ảnh chính
curl -X PUT http://localhost:8080/api/admin/products/1/images/2/primary \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Test sắp xếp lại thứ tự ảnh
curl -X PUT http://localhost:8080/api/admin/products/1/images/reorder \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '[3, 1, 2]'

# Test xóa ảnh theo ID
curl -X DELETE http://localhost:8080/api/admin/products/images/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Test lấy thống kê dashboard
curl -X GET http://localhost:8080/api/admin/dashboard/stats \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Test tạo thông tin vận chuyển
curl -X POST http://localhost:8080/api/admin/shipping \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "orderId": 1,
    "shippingMethod": "GHTK",
    "fromAddress": "123 ABC Street, Ho Chi Minh City",
    "toAddress": "456 XYZ Street, Hanoi",
    "customerName": "John Doe",
    "customerPhone": "0123456789",
    "shippingFee": 30000
  }'

# Test lấy danh sách vận chuyển
curl -X GET http://localhost:8080/api/admin/shipping \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 2. Sử dụng Postman Collection

Tạo Postman Collection với các request sau:

#### 2.1. Authentication Setup
- **Method**: POST
- **URL**: `http://localhost:8080/api/auth/login`
- **Headers**: `Content-Type: application/json`
- **Body**:
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```
- **Tests Script** (để lưu token):
```javascript
if (pm.response.code === 200) {
    const response = pm.response.json();
    pm.environment.set("jwt_token", response.data.token);
}
```

#### 2.2. Product Management
- **Create Product**:
  - Method: POST
  - URL: `http://localhost:8080/api/admin/products`
  - Headers: `Authorization: Bearer {{jwt_token}}`, `Content-Type: application/json`
  - Body: (sử dụng JSON từ curl example ở trên)

- **Get Products**:
  - Method: GET
  - URL: `http://localhost:8080/api/admin/products`
  - Headers: `Authorization: Bearer {{jwt_token}}`

#### 2.3. Product Image Management
- **Get Product Images**:
  - Method: GET
  - URL: `http://localhost:8080/api/admin/products/1/images`
  - Headers: `Authorization: Bearer {{jwt_token}}`

- **Add Product Image**:
  - Method: POST
  - URL: `http://localhost:8080/api/admin/products/1/images`
  - Headers: `Authorization: Bearer {{jwt_token}}`
  - Body (form-data):
    - `imageUrl`: `https://example.com/product-image-1.jpg`
    - `altText`: `Ảnh sản phẩm chính`

- **Update Product Image**:
  - Method: PUT
  - URL: `http://localhost:8080/api/admin/products/images/1`
  - Headers: `Authorization: Bearer {{jwt_token}}`
  - Body (form-data):
    - `altText`: `Ảnh sản phẩm đã cập nhật`
    - `sortOrder`: `0`

- **Set Primary Image**:
  - Method: PUT
  - URL: `http://localhost:8080/api/admin/products/1/images/2/primary`
  - Headers: `Authorization: Bearer {{jwt_token}}`

- **Reorder Images**:
  - Method: PUT
  - URL: `http://localhost:8080/api/admin/products/1/images/reorder`
  - Headers: `Authorization: Bearer {{jwt_token}}`, `Content-Type: application/json`
  - Body: `[3, 1, 2]`

- **Delete Image**:
  - Method: DELETE
  - URL: `http://localhost:8080/api/admin/products/images/1`
  - Headers: `Authorization: Bearer {{jwt_token}}`

### 3. Sử dụng Unit Tests

Chạy test:
```bash
cd trangsuc_js
./mvnw test -Dtest=AdminControllerTest
```

### 3. Sử dụng Integration Tests

```bash
# Chạy tất cả tests
./mvnw test

# Chạy test cụ thể
./mvnw test -Dtest=AdminControllerTest#testCreateProduct
```

## Lưu ý

1. **Authentication**: Tất cả API Admin đều yêu cầu JWT token với role ADMIN
2. **Pagination**: Các API list đều hỗ trợ phân trang với tham số `page` và `size`
3. **Error Handling**: API trả về lỗi 401 nếu không có quyền, 404 nếu không tìm thấy resource
4. **File Upload**: API upload hình ảnh sản phẩm sử dụng multipart/form-data
5. **Export**: API export trả về file Excel với Content-Disposition header

## Trạng thái Implementation

✅ **Hoàn thành**:
- **Product Management**: CRUD operations + Image Upload endpoints
- **Product Image Management**: Quản lý nhiều ảnh cho sản phẩm, đặt ảnh chính, sắp xếp thứ tự
- **Category Management**: CRUD operations với các trường mới (description, imageUrl, isActive, sortOrder)
- **User Management**: Quản lý người dùng với thông tin đầy đủ (phone, address, preferences)
- **Order Management**: Quản lý đơn hàng với thông tin thanh toán và vận chuyển
- **Inventory Management**: Quản lý kho hàng, cảnh báo hết hàng
- **Promotion Management**: Quản lý khuyến mãi, Flash Sale, Coupon
- **Shipping Management**: Quản lý vận chuyển, tích hợp GHTK/GHN/Viettel Post
- **Dashboard & Statistics**: Thống kê doanh thu, top sản phẩm, khách hàng
- **Export Reports**: Xuất báo cáo Excel cho orders/products/customers
- **AdminController**: Tất cả endpoints với proper HTTP status codes
- **AdminService Interface**: Tất cả method signatures
- **AdminServiceImpl**: Logic thực tế cho ProductImage management
- **Unit Tests**: Test suite cho tất cả endpoints bao gồm ProductImage management
- **DTO Classes**: Cho tất cả entities với validation constraints
- **Entity Classes**: Product, ProductImage, Promotion, Shipping với proper JPA annotations
- **Repository Classes**: ProductImageRepository, PromotionRepository, ShippingRepository
- **Validation**: Proper constraints cho tất cả DTOs

🔄 **Cần implement logic**:
- File upload logic cho product images (MultipartFile handling)
- Excel export functionality (Apache POI)
- Dashboard statistics calculation
- Integration với shipping providers (GHTK, GHN, Viettel Post APIs)
- Complete AdminServiceImpl implementation cho các method còn lại

📋 **Có thể mở rộng**:
- Real-time notifications cho inventory alerts
- Advanced reporting với charts/graphs
- Bulk operations cho products/orders
- Advanced search và filtering
- Audit logging cho admin actions
