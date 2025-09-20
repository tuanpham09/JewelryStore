# 📚 API Documentation - JewelryStore

## 🔐 Authentication APIs

### POST `/api/auth/register`
Đăng ký tài khoản mới

**Request Body:**
```json
{
  "fullName": "Nguyễn Văn A",
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### POST `/api/auth/login`
Đăng nhập

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### GET `/api/auth/me`
Lấy thông tin user hiện tại

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "User details retrieved",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn A",
    "roles": ["ROLE_USER"]
  }
}
```

---

## 🛍️ Product APIs

### GET `/api/products`
Lấy danh sách tất cả sản phẩm

**Response:**
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Nhẫn vàng 18k",
      "slug": "nhan-vang-18k",
      "description": "Nhẫn vàng 18k đẹp...",
      "price": 5000000,
      "stock": 10,
      "thumbnail": "https://example.com/image.jpg",
      "categoryName": "Nhẫn",
      "averageRating": 4.5,
      "reviewCount": 12
    }
  ]
}
```

### GET `/api/products/{id}`
Lấy chi tiết sản phẩm

**Response:**
```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "name": "Nhẫn vàng 18k",
    "slug": "nhan-vang-18k",
    "description": "Nhẫn vàng 18k đẹp...",
    "price": 5000000,
    "stock": 10,
    "thumbnail": "https://example.com/image.jpg",
    "categoryName": "Nhẫn",
    "averageRating": 4.5,
    "reviewCount": 12
  }
}
```

### GET `/api/products/category/{categoryId}`
Lấy sản phẩm theo danh mục

**Response:** Tương tự GET `/api/products`

---

## 🛒 Cart APIs

### GET `/api/cart`
Lấy giỏ hàng của user hiện tại

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 15000000,
  "itemCount": 3,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:30:00",
  "items": [
    {
      "id": 1,
      "cartId": 1,
      "productId": 1,
      "productName": "Nhẫn vàng 18k",
      "productSku": "N18K001",
      "productImage": "https://example.com/image.jpg",
      "unitPrice": 5000000,
      "quantity": 2,
      "subtotal": 10000000,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

### POST `/api/cart/items`
Thêm sản phẩm vào giỏ hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

**Response:** Tương tự GET `/api/cart`

### DELETE `/api/cart/items/{productId}`
Xóa sản phẩm khỏi giỏ hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Response:** Tương tự GET `/api/cart`

---

## 📦 Order APIs

### POST `/api/orders/checkout`
Thanh toán đơn hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "address": "123 Đường ABC, Quận 1, TP.HCM"
}
```

**Response:**
```json
{
  "id": 1,
  "status": "PENDING",
  "total": 15000000,
  "createdAt": "2024-01-01T10:00:00",
  "items": [
    {
      "productId": 1,
      "productName": "Nhẫn vàng 18k",
      "quantity": 2,
      "price": 5000000
    }
  ]
}
```

### GET `/api/orders/mine`
Lấy lịch sử đơn hàng của user

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": 1,
    "status": "DELIVERED",
    "total": 15000000,
    "createdAt": "2024-01-01T10:00:00",
    "items": [
      {
        "productId": 1,
        "productName": "Nhẫn vàng 18k",
        "quantity": 2,
        "price": 5000000
      }
    ]
  }
]
```

---

## ⭐ Review APIs

### POST `/api/reviews`
Thêm đánh giá sản phẩm

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "productId": 1,
  "rating": 5,
  "comment": "Sản phẩm rất đẹp, chất lượng tốt"
}
```

**Response:**
```json
{
  "message": "Review added successfully"
}
```

### GET `/api/reviews/product/{productId}`
Lấy đánh giá của sản phẩm

**Response:**
```json
[
  {
    "id": 1,
    "userName": "Nguyễn Văn A",
    "rating": 5,
    "comment": "Sản phẩm rất đẹp, chất lượng tốt",
    "createdAt": "2024-01-01T10:00:00"
  }
]
```

---

## 🛠️ Admin APIs

### 📦 Product Management

#### POST `/api/admin/products`
Tạo sản phẩm mới

**Headers:**
```
Authorization: Bearer <admin_token>
```

**Request Body:**
```json
{
  "name": "Nhẫn vàng 18k",
  "slug": "nhan-vang-18k",
  "description": "Nhẫn vàng 18k đẹp...",
  "shortDescription": "Nhẫn vàng cao cấp",
  "price": 5000000,
  "originalPrice": 6000000,
  "salePrice": 5000000,
  "stock": 10,
  "minStock": 5,
  "maxStock": 100,
  "sku": "N18K001",
  "barcode": "123456789",
  "weight": 5.5,
  "dimensions": "2x2x1 cm",
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
  "metaDescription": "Nhẫn vàng 18k cao cấp...",
  "metaKeywords": "nhẫn, vàng, 18k, trang sức",
  "thumbnail": "https://example.com/thumbnail.jpg",
  "categoryId": 1
}
```

#### PUT `/api/admin/products/{id}`
Cập nhật sản phẩm

#### DELETE `/api/admin/products/{id}`
Xóa sản phẩm

#### GET `/api/admin/products`
Lấy danh sách sản phẩm (Admin view)

### 🖼️ Product Image Management

#### GET `/api/admin/products/{id}/images`
Lấy danh sách hình ảnh sản phẩm

#### POST `/api/admin/products/{id}/images`
Thêm hình ảnh sản phẩm

**Request Body (multipart/form-data):**
```
file: <image_file>
```

#### POST `/api/admin/products/{id}/images`
Thêm hình ảnh bằng URL

**Request Body:**
```
imageUrl: "https://example.com/image.jpg"
altText: "Mô tả hình ảnh"
```

#### PUT `/api/admin/products/images/{imageId}`
Cập nhật thông tin hình ảnh

#### DELETE `/api/admin/products/images/{imageId}`
Xóa hình ảnh

#### PUT `/api/admin/products/{id}/images/{imageId}/primary`
Đặt hình ảnh làm ảnh chính

#### PUT `/api/admin/products/{id}/images/reorder`
Sắp xếp lại thứ tự hình ảnh

### 📂 Category Management

#### POST `/api/admin/categories`
Tạo danh mục mới

**Request Body:**
```json
{
  "name": "Nhẫn",
  "slug": "nhan",
  "description": "Danh mục nhẫn vàng, bạc...",
  "imageUrl": "https://example.com/category.jpg",
  "isActive": true,
  "sortOrder": 1
}
```

#### PUT `/api/admin/categories/{id}`
Cập nhật danh mục

#### DELETE `/api/admin/categories/{id}`
Xóa danh mục

#### GET `/api/admin/categories`
Lấy danh sách danh mục

### 👥 User Management

#### GET `/api/admin/users`
Lấy danh sách người dùng

**Query Parameters:**
- `page`: Số trang (default: 0)
- `size`: Số lượng per page (default: 20)

#### GET `/api/admin/users/{id}`
Lấy thông tin chi tiết người dùng

#### PUT `/api/admin/users/{id}/status`
Cập nhật trạng thái người dùng

**Request Body:**
```
enabled: true/false
```

#### PUT `/api/admin/users/{id}/roles`
Cập nhật quyền người dùng

**Request Body:**
```json
["ROLE_USER", "ROLE_STAFF"]
```

#### DELETE `/api/admin/users/{id}`
Xóa người dùng

### 📋 Order Management

#### GET `/api/admin/orders`
Lấy danh sách đơn hàng

#### GET `/api/admin/orders/{id}`
Lấy chi tiết đơn hàng

#### PUT `/api/admin/orders/{id}/status`
Cập nhật trạng thái đơn hàng

**Request Body:**
```
status: "PROCESSING" | "SHIPPED" | "DELIVERED" | "CANCELLED"
```

#### PUT `/api/admin/orders/{id}/cancel`
Hủy đơn hàng

**Request Body:**
```
reason: "Lý do hủy đơn"
```

#### GET `/api/admin/orders/status/{status}`
Lấy đơn hàng theo trạng thái

### 📊 Inventory Management

#### GET `/api/admin/inventory`
Lấy trạng thái tồn kho

#### GET `/api/admin/inventory/low-stock`
Lấy sản phẩm sắp hết hàng

#### GET `/api/admin/inventory/out-of-stock`
Lấy sản phẩm hết hàng

#### PUT `/api/admin/inventory/{productId}/stock`
Cập nhật số lượng tồn kho

**Request Body:**
```
newStock: 50
```

#### PUT `/api/admin/inventory/{productId}/min-max-stock`
Cập nhật min/max stock

**Request Body:**
```
minStock: 5
maxStock: 100
```

### 🎁 Promotion Management

#### POST `/api/admin/promotions`
Tạo khuyến mãi mới

**Request Body:**
```json
{
  "name": "Giảm giá 20%",
  "description": "Giảm giá 20% cho tất cả sản phẩm",
  "type": "PERCENTAGE",
  "value": 20,
  "minOrderAmount": 1000000,
  "code": "SALE20",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "active": true,
  "usageLimit": 100,
  "usedCount": 0,
  "applicableProducts": "ALL",
  "applicableCategories": "1,2,3",
  "applicableProductIds": "1,2,3"
}
```

#### PUT `/api/admin/promotions/{id}`
Cập nhật khuyến mãi

#### DELETE `/api/admin/promotions/{id}`
Xóa khuyến mãi

#### GET `/api/admin/promotions`
Lấy danh sách khuyến mãi

#### PUT `/api/admin/promotions/{id}/toggle`
Bật/tắt khuyến mãi

### 📈 Dashboard & Statistics

#### GET `/api/admin/dashboard/stats`
Lấy thống kê dashboard

**Response:**
```json
{
  "totalRevenue": 100000000,
  "totalOrders": 150,
  "totalCustomers": 75,
  "totalProducts": 200,
  "lowStockProducts": 5,
  "outOfStockProducts": 2,
  "activePromotions": 3
}
```

#### GET `/api/admin/reports/revenue/date-range`
Lấy báo cáo doanh thu theo khoảng thời gian

**Query Parameters:**
- `startDate`: "2024-01-01"
- `endDate`: "2024-01-31"

### 🚚 Shipping Management

#### GET `/api/admin/shipping`
Lấy danh sách vận chuyển

#### GET `/api/admin/shipping/{id}`
Lấy chi tiết vận chuyển

#### GET `/api/admin/shipping/order/{orderId}`
Lấy thông tin vận chuyển theo đơn hàng

#### POST `/api/admin/shipping`
Tạo thông tin vận chuyển

#### PUT `/api/admin/shipping/{id}/status`
Cập nhật trạng thái vận chuyển

#### PUT `/api/admin/shipping/{id}/tracking`
Cập nhật mã tracking

### 📊 Export Reports

#### GET `/api/admin/reports/orders/export`
Xuất báo cáo đơn hàng Excel

**Query Parameters:**
- `startDate`: "2024-01-01"
- `endDate`: "2024-01-31"

**Response:** Excel file download

#### GET `/api/admin/reports/products/export`
Xuất báo cáo sản phẩm Excel

#### GET `/api/admin/reports/customers/export`
Xuất báo cáo khách hàng Excel

---

## 🔒 Authentication & Authorization

### JWT Token
- Tất cả API (trừ auth) yêu cầu header: `Authorization: Bearer <token>`
- Token có thời hạn 1 giờ
- Admin APIs yêu cầu role `ROLE_ADMIN`

### Error Responses
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error message",
  "timestamp": "2024-01-01T10:00:00"
}
```

### Common HTTP Status Codes
- `200`: Success
- `201`: Created
- `400`: Bad Request
- `401`: Unauthorized
- `403`: Forbidden
- `404`: Not Found
- `500`: Internal Server Error

---

## 📝 Notes
- Tất cả API sử dụng JSON format
- Timestamps theo format ISO 8601
- Giá tiền được trả về dưới dạng số (VND)
- Pagination sử dụng page-based với default page=0, size=20
