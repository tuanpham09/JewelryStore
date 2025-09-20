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
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "fullName": "Nguyễn Văn A",
      "roles": ["ROLE_USER"]
    }
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
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "fullName": "Nguyễn Văn A",
      "roles": ["ROLE_USER"]
    }
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

## 👤 Customer Profile Management APIs

### GET `/api/customer/profile`
Lấy thông tin profile của khách hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Profile retrieved successfully",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn A",
    "phone": "0123456789",
    "dateOfBirth": "1990-01-01",
    "gender": "MALE",
    "avatar": "https://example.com/avatar.jpg",
    "addresses": [
      {
        "id": 1,
        "fullName": "Nguyễn Văn A",
        "phone": "0123456789",
        "address": "123 Đường ABC, Quận 1",
        "city": "TP.HCM",
        "district": "Quận 1",
        "ward": "Phường Bến Nghé",
        "postalCode": "700000",
        "isDefault": true
      }
    ],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### PUT `/api/customer/profile`
Cập nhật thông tin profile

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "fullName": "Nguyễn Văn B",
  "phone": "0987654321",
  "dateOfBirth": "1990-01-01",
  "gender": "FEMALE"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn B",
    "phone": "0987654321",
    "dateOfBirth": "1990-01-01",
    "gender": "FEMALE",
    "avatar": "https://example.com/avatar.jpg",
    "addresses": [],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T11:00:00"
  }
}
```

### PUT `/api/customer/password`
Đổi mật khẩu

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "currentPassword": "oldpassword123",
  "newPassword": "newpassword123",
  "confirmPassword": "newpassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Password changed successfully",
  "data": null
}
```

### POST `/api/customer/avatar`
Upload avatar

**Headers:**
```
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**Request Body (multipart/form-data):**
```
file: <image_file>
```

**Response:**
```json
{
  "success": true,
  "message": "Avatar uploaded successfully",
  "data": "https://example.com/avatar/new-avatar.jpg"
}
```

---

## 🏠 Address Management APIs

### GET `/api/customer/addresses`
Lấy danh sách địa chỉ của khách hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Addresses retrieved successfully",
  "data": [
    {
      "id": 1,
      "fullName": "Nguyễn Văn A",
      "phone": "0123456789",
      "address": "123 Đường ABC, Quận 1",
      "city": "TP.HCM",
      "district": "Quận 1",
      "ward": "Phường Bến Nghé",
      "postalCode": "700000",
      "isDefault": true,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    },
    {
      "id": 2,
      "fullName": "Nguyễn Văn A",
      "phone": "0123456789",
      "address": "456 Đường XYZ, Quận 2",
      "city": "TP.HCM",
      "district": "Quận 2",
      "ward": "Phường Thủ Thiêm",
      "postalCode": "700000",
      "isDefault": false,
      "createdAt": "2024-01-01T11:00:00",
      "updatedAt": "2024-01-01T11:00:00"
    }
  ]
}
```

### POST `/api/customer/addresses`
Thêm địa chỉ mới

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "fullName": "Nguyễn Văn A",
  "phone": "0123456789",
  "address": "789 Đường DEF, Quận 3",
  "city": "TP.HCM",
  "district": "Quận 3",
  "ward": "Phường Võ Thị Sáu",
  "postalCode": "700000",
  "isDefault": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Address added successfully",
  "data": {
    "id": 3,
    "fullName": "Nguyễn Văn A",
    "phone": "0123456789",
    "address": "789 Đường DEF, Quận 3",
    "city": "TP.HCM",
    "district": "Quận 3",
    "ward": "Phường Võ Thị Sáu",
    "postalCode": "700000",
    "isDefault": false,
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T12:00:00"
  }
}
```

### PUT `/api/customer/addresses/{id}`
Cập nhật địa chỉ

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "fullName": "Nguyễn Văn B",
  "phone": "0987654321",
  "address": "789 Đường DEF, Quận 3",
  "city": "TP.HCM",
  "district": "Quận 3",
  "ward": "Phường Võ Thị Sáu",
  "postalCode": "700000",
  "isDefault": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Address updated successfully",
  "data": {
    "id": 3,
    "fullName": "Nguyễn Văn B",
    "phone": "0987654321",
    "address": "789 Đường DEF, Quận 3",
    "city": "TP.HCM",
    "district": "Quận 3",
    "ward": "Phường Võ Thị Sáu",
    "postalCode": "700000",
    "isDefault": false,
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T13:00:00"
  }
}
```

### DELETE `/api/customer/addresses/{id}`
Xóa địa chỉ

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Address deleted successfully",
  "data": null
}
```

### PUT `/api/customer/addresses/{id}/default`
Đặt địa chỉ làm mặc định

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Default address set successfully",
  "data": {
    "id": 3,
    "fullName": "Nguyễn Văn B",
    "phone": "0987654321",
    "address": "789 Đường DEF, Quận 3",
    "city": "TP.HCM",
    "district": "Quận 3",
    "ward": "Phường Võ Thị Sáu",
    "postalCode": "700000",
    "isDefault": true,
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T14:00:00"
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
    },
    {
      "id": 2,
      "cartId": 1,
      "productId": 2,
      "productName": "Dây chuyền bạc",
      "productSku": "DCB001",
      "productImage": "https://example.com/necklace.jpg",
      "unitPrice": 2500000,
      "quantity": 2,
      "subtotal": 5000000,
      "createdAt": "2024-01-01T10:15:00",
      "updatedAt": "2024-01-01T10:15:00"
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
  "productId": 3,
  "quantity": 1
}
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "totalAmount": 20000000,
  "itemCount": 4,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:45:00",
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
    },
    {
      "id": 2,
      "cartId": 1,
      "productId": 2,
      "productName": "Dây chuyền bạc",
      "productSku": "DCB001",
      "productImage": "https://example.com/necklace.jpg",
      "unitPrice": 2500000,
      "quantity": 2,
      "subtotal": 5000000,
      "createdAt": "2024-01-01T10:15:00",
      "updatedAt": "2024-01-01T10:15:00"
    },
    {
      "id": 3,
      "cartId": 1,
      "productId": 3,
      "productName": "Bông tai kim cương",
      "productSku": "BEK001",
      "productImage": "https://example.com/earrings.jpg",
      "unitPrice": 5000000,
      "quantity": 1,
      "subtotal": 5000000,
      "createdAt": "2024-01-01T10:45:00",
      "updatedAt": "2024-01-01T10:45:00"
    }
  ]
}
```

### DELETE `/api/cart/items/{productId}`
Xóa sản phẩm khỏi giỏ hàng

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
  "updatedAt": "2024-01-01T10:50:00",
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
    },
    {
      "id": 2,
      "cartId": 1,
      "productId": 2,
      "productName": "Dây chuyền bạc",
      "productSku": "DCB001",
      "productImage": "https://example.com/necklace.jpg",
      "unitPrice": 2500000,
      "quantity": 2,
      "subtotal": 5000000,
      "createdAt": "2024-01-01T10:15:00",
      "updatedAt": "2024-01-01T10:15:00"
    }
  ]
}
```

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
  "addressId": 1,
  "paymentMethod": "CREDIT_CARD",
  "notes": "Giao hàng vào buổi chiều"
}
```

**Response:**
```json
{
  "id": 1,
  "orderNumber": "ORD-20240101-001",
  "status": "PENDING",
  "total": 15000000,
  "shippingAddress": {
    "id": 1,
    "fullName": "Nguyễn Văn A",
    "phone": "0123456789",
    "address": "123 Đường ABC, Quận 1",
    "city": "TP.HCM",
    "district": "Quận 1",
    "ward": "Phường Bến Nghé",
    "postalCode": "700000"
  },
  "paymentMethod": "CREDIT_CARD",
  "notes": "Giao hàng vào buổi chiều",
  "createdAt": "2024-01-01T10:00:00",
  "items": [
    {
      "productId": 1,
      "productName": "Nhẫn vàng 18k",
      "productSku": "N18K001",
      "quantity": 2,
      "unitPrice": 5000000,
      "subtotal": 10000000
    },
    {
      "productId": 2,
      "productName": "Dây chuyền bạc",
      "productSku": "DCB001",
      "quantity": 1,
      "unitPrice": 5000000,
      "subtotal": 5000000
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
    "orderNumber": "ORD-20240101-001",
    "status": "DELIVERED",
    "total": 15000000,
    "shippingAddress": {
      "id": 1,
      "fullName": "Nguyễn Văn A",
      "phone": "0123456789",
      "address": "123 Đường ABC, Quận 1",
      "city": "TP.HCM",
      "district": "Quận 1",
      "ward": "Phường Bến Nghé",
      "postalCode": "700000"
    },
    "paymentMethod": "CREDIT_CARD",
    "createdAt": "2024-01-01T10:00:00",
    "deliveredAt": "2024-01-03T14:30:00",
    "items": [
      {
        "productId": 1,
        "productName": "Nhẫn vàng 18k",
        "productSku": "N18K001",
        "quantity": 2,
        "unitPrice": 5000000,
        "subtotal": 10000000
      },
      {
        "productId": 2,
        "productName": "Dây chuyền bạc",
        "productSku": "DCB001",
        "quantity": 1,
        "unitPrice": 5000000,
        "subtotal": 5000000
      }
    ]
  },
  {
    "id": 2,
    "orderNumber": "ORD-20240102-002",
    "status": "PROCESSING",
    "total": 8000000,
    "shippingAddress": {
      "id": 2,
      "fullName": "Nguyễn Văn A",
      "phone": "0123456789",
      "address": "456 Đường XYZ, Quận 2",
      "city": "TP.HCM",
      "district": "Quận 2",
      "ward": "Phường Thủ Thiêm",
      "postalCode": "700000"
    },
    "paymentMethod": "BANK_TRANSFER",
    "createdAt": "2024-01-02T09:00:00",
    "items": [
      {
        "productId": 3,
        "productName": "Bông tai kim cương",
        "productSku": "BEK001",
        "quantity": 1,
        "unitPrice": 8000000,
        "subtotal": 8000000
      }
    ]
  }
]
```

### GET `/api/orders/track/{orderId}`
Theo dõi đơn hàng theo ID

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Order tracking retrieved successfully",
  "data": {
    "orderId": 1,
    "orderNumber": "ORD-20240101-001",
    "status": "SHIPPED",
    "currentStatus": "SHIPPED",
    "trackingNumber": "VN123456789",
    "shippingCompany": "Viettel Post",
    "estimatedDelivery": "2024-01-03T18:00:00",
    "timeline": [
      {
        "status": "PENDING",
        "description": "Đơn hàng đã được tạo",
        "timestamp": "2024-01-01T10:00:00"
      },
      {
        "status": "CONFIRMED",
        "description": "Đơn hàng đã được xác nhận",
        "timestamp": "2024-01-01T11:30:00"
      },
      {
        "status": "PROCESSING",
        "description": "Đơn hàng đang được xử lý",
        "timestamp": "2024-01-01T14:00:00"
      },
      {
        "status": "SHIPPED",
        "description": "Đơn hàng đã được gửi đi",
        "timestamp": "2024-01-02T09:00:00"
      }
    ],
    "shippingAddress": {
      "fullName": "Nguyễn Văn A",
      "phone": "0123456789",
      "address": "123 Đường ABC, Quận 1, TP.HCM"
    }
  }
}
```

### GET `/api/orders/track/number/{orderNumber}`
Theo dõi đơn hàng theo số đơn hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Response:** Tương tự GET `/api/orders/track/{orderId}`

### GET `/api/orders/my-orders`
Lấy danh sách đơn hàng với thông tin tracking

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Orders retrieved successfully",
  "data": [
    {
      "orderId": 1,
      "orderNumber": "ORD-20240101-001",
      "status": "DELIVERED",
      "currentStatus": "DELIVERED",
      "total": 15000000,
      "trackingNumber": "VN123456789",
      "shippingCompany": "Viettel Post",
      "createdAt": "2024-01-01T10:00:00",
      "deliveredAt": "2024-01-03T14:30:00",
      "items": [
        {
          "productId": 1,
          "productName": "Nhẫn vàng 18k",
          "quantity": 2,
          "unitPrice": 5000000
        }
      ]
    }
  ]
}
```

### POST `/api/orders/cancel`
Hủy đơn hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "orderId": 2,
  "reason": "Thay đổi ý định mua hàng"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Order cancelled successfully",
  "data": {
    "orderId": 2,
    "orderNumber": "ORD-20240102-002",
    "status": "CANCELLED",
    "currentStatus": "CANCELLED",
    "cancelledAt": "2024-01-02T15:00:00",
    "cancellationReason": "Thay đổi ý định mua hàng",
    "refundStatus": "PENDING"
  }
}
```

### POST `/api/orders/{orderId}/return`
Yêu cầu trả hàng

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```
reason: "Sản phẩm không đúng mô tả"
```

**Response:**
```json
{
  "success": true,
  "message": "Return request submitted successfully",
  "data": {
    "orderId": 1,
    "orderNumber": "ORD-20240101-001",
    "status": "RETURN_REQUESTED",
    "currentStatus": "RETURN_REQUESTED",
    "returnRequestedAt": "2024-01-04T10:00:00",
    "returnReason": "Sản phẩm không đúng mô tả",
    "returnStatus": "PENDING_APPROVAL"
  }
}
```

---

## 💳 Payment APIs

### GET `/api/payments/methods`
Lấy danh sách phương thức thanh toán

**Response:**
```json
{
  "success": true,
  "message": "Payment methods retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Credit Card",
      "code": "CREDIT_CARD",
      "description": "Thanh toán bằng thẻ tín dụng",
      "icon": "https://example.com/credit-card-icon.png",
      "isActive": true,
      "processingFee": 0.03,
      "minAmount": 100000,
      "maxAmount": 50000000
    },
    {
      "id": 2,
      "name": "Bank Transfer",
      "code": "BANK_TRANSFER",
      "description": "Chuyển khoản ngân hàng",
      "icon": "https://example.com/bank-icon.png",
      "isActive": true,
      "processingFee": 0,
      "minAmount": 100000,
      "maxAmount": 100000000
    },
    {
      "id": 3,
      "name": "Cash on Delivery",
      "code": "COD",
      "description": "Thanh toán khi nhận hàng",
      "icon": "https://example.com/cod-icon.png",
      "isActive": true,
      "processingFee": 0.05,
      "minAmount": 100000,
      "maxAmount": 20000000
    }
  ]
}
```

### POST `/api/payments/process`
Xử lý thanh toán

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "orderId": 1,
  "paymentMethod": "CREDIT_CARD",
  "amount": 15000000,
  "currency": "VND",
  "paymentDetails": {
    "cardNumber": "4111111111111111",
    "expiryMonth": "12",
    "expiryYear": "2025",
    "cvv": "123",
    "cardholderName": "NGUYEN VAN A"
  },
  "billingAddress": {
    "fullName": "Nguyễn Văn A",
    "address": "123 Đường ABC, Quận 1",
    "city": "TP.HCM",
    "postalCode": "700000"
  }
}
```

**Response:**
```json
{
  "success": true,
  "message": "Payment processed successfully",
  "data": {
    "paymentId": "PAY-20240101-001",
    "orderId": 1,
    "status": "COMPLETED",
    "amount": 15000000,
    "currency": "VND",
    "paymentMethod": "CREDIT_CARD",
    "transactionId": "TXN123456789",
    "gatewayResponse": {
      "gateway": "VNPay",
      "gatewayTransactionId": "VN123456789",
      "gatewayStatus": "SUCCESS"
    },
    "processedAt": "2024-01-01T10:30:00",
    "receiptUrl": "https://example.com/receipts/PAY-20240101-001.pdf"
  }
}
```

### GET `/api/payments/{paymentId}/status`
Kiểm tra trạng thái thanh toán

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Payment status retrieved successfully",
  "data": {
    "paymentId": "PAY-20240101-001",
    "orderId": 1,
    "status": "COMPLETED",
    "amount": 15000000,
    "currency": "VND",
    "paymentMethod": "CREDIT_CARD",
    "transactionId": "TXN123456789",
    "processedAt": "2024-01-01T10:30:00",
    "gatewayResponse": {
      "gateway": "VNPay",
      "gatewayTransactionId": "VN123456789",
      "gatewayStatus": "SUCCESS"
    }
  }
}
```

### POST `/api/payments/{paymentId}/cancel`
Hủy thanh toán

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Payment cancelled successfully",
  "data": {
    "paymentId": "PAY-20240101-001",
    "orderId": 1,
    "status": "CANCELLED",
    "amount": 15000000,
    "currency": "VND",
    "paymentMethod": "CREDIT_CARD",
    "cancelledAt": "2024-01-01T10:35:00",
    "refundStatus": "PENDING"
  }
}
```

### POST `/api/payments/{paymentId}/refund`
Hoàn tiền

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```
reason: "Khách hàng yêu cầu hoàn tiền"
```

**Response:**
```json
{
  "success": true,
  "message": "Refund processed successfully",
  "data": {
    "paymentId": "PAY-20240101-001",
    "orderId": 1,
    "status": "REFUNDED",
    "amount": 15000000,
    "currency": "VND",
    "paymentMethod": "CREDIT_CARD",
    "refundId": "REF-20240101-001",
    "refundAmount": 15000000,
    "refundReason": "Khách hàng yêu cầu hoàn tiền",
    "refundedAt": "2024-01-01T11:00:00",
    "estimatedRefundTime": "3-5 ngày làm việc"
  }
}
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

## ❤️ Wishlist APIs

### GET `/api/wishlist`
Lấy danh sách sản phẩm yêu thích

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Wishlist retrieved successfully",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Nhẫn vàng 18k",
      "productSku": "N18K001",
      "productImage": "https://example.com/image.jpg",
      "price": 5000000,
      "originalPrice": 6000000,
      "salePrice": 5000000,
      "discount": 16.67,
      "isOnSale": true,
      "stock": 10,
      "addedAt": "2024-01-01T10:00:00",
      "priceDropNotification": {
        "enabled": true,
        "targetPrice": 4500000,
        "notified": false
      }
    },
    {
      "id": 2,
      "productId": 3,
      "productName": "Bông tai kim cương",
      "productSku": "BEK001",
      "productImage": "https://example.com/earrings.jpg",
      "price": 8000000,
      "originalPrice": 8000000,
      "salePrice": null,
      "discount": 0,
      "isOnSale": false,
      "stock": 5,
      "addedAt": "2024-01-01T11:00:00",
      "priceDropNotification": {
        "enabled": true,
        "targetPrice": 7000000,
        "notified": true
      }
    }
  ]
}
```

### POST `/api/wishlist/{productId}`
Thêm sản phẩm vào danh sách yêu thích

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Product added to wishlist successfully",
  "data": {
    "id": 3,
    "productId": 5,
    "productName": "Dây chuyền vàng 24k",
    "productSku": "DCV24K001",
    "productImage": "https://example.com/necklace-gold.jpg",
    "price": 12000000,
    "originalPrice": 12000000,
    "salePrice": null,
    "discount": 0,
    "isOnSale": false,
    "stock": 3,
    "addedAt": "2024-01-01T12:00:00",
    "priceDropNotification": {
      "enabled": false,
      "targetPrice": null,
      "notified": false
    }
  }
}
```

### DELETE `/api/wishlist/{productId}`
Xóa sản phẩm khỏi danh sách yêu thích

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Product removed from wishlist successfully",
  "data": null
}
```

### GET `/api/wishlist/{productId}/check`
Kiểm tra sản phẩm có trong danh sách yêu thích không

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Wishlist status checked successfully",
  "data": true
}
```

### GET `/api/wishlist/notifications`
Lấy thông báo giảm giá cho sản phẩm yêu thích

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Price drop notifications retrieved successfully",
  "data": [
    {
      "id": 2,
      "productId": 3,
      "productName": "Bông tai kim cương",
      "productSku": "BEK001",
      "productImage": "https://example.com/earrings.jpg",
      "oldPrice": 8000000,
      "newPrice": 7000000,
      "discount": 12.5,
      "priceDropAmount": 1000000,
      "notificationSentAt": "2024-01-01T15:00:00",
      "addedAt": "2024-01-01T11:00:00"
    }
  ]
}
```

---

## 🔍 Search APIs

### POST `/api/search/products`
Tìm kiếm sản phẩm

**Request Body:**
```json
{
  "query": "nhẫn vàng",
  "categoryId": 1,
  "minPrice": 1000000,
  "maxPrice": 10000000,
  "brand": "JewelryStore",
  "material": "Vàng 18k",
  "color": "Vàng",
  "sortBy": "price",
  "sortOrder": "asc",
  "page": 0,
  "size": 20
}
```

**Response:**
```json
{
  "success": true,
  "message": "Products searched successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Nhẫn vàng 18k",
        "slug": "nhan-vang-18k",
        "description": "Nhẫn vàng 18k đẹp...",
        "price": 5000000,
        "originalPrice": 6000000,
        "salePrice": 5000000,
        "stock": 10,
        "thumbnail": "https://example.com/image.jpg",
        "categoryName": "Nhẫn",
        "brand": "JewelryStore",
        "material": "Vàng 18k",
        "color": "Vàng",
        "averageRating": 4.5,
        "reviewCount": 12,
        "isFeatured": true,
        "isNew": false,
        "isBestseller": true,
        "isOnSale": true
      }
    ],
    "totalElements": 25,
    "totalPages": 2,
    "currentPage": 0,
    "size": 20,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

### GET `/api/search/products/featured`
Lấy sản phẩm nổi bật

**Query Parameters:**
- `page`: Số trang (default: 0)
- `size`: Số lượng per page (default: 20)

**Response:**
```json
{
  "success": true,
  "message": "Featured products retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Nhẫn vàng 18k",
        "slug": "nhan-vang-18k",
        "description": "Nhẫn vàng 18k đẹp...",
        "price": 5000000,
        "originalPrice": 6000000,
        "salePrice": 5000000,
        "stock": 10,
        "thumbnail": "https://example.com/image.jpg",
        "categoryName": "Nhẫn",
        "averageRating": 4.5,
        "reviewCount": 12,
        "isFeatured": true,
        "isNew": false,
        "isBestseller": true,
        "isOnSale": true
      }
    ],
    "totalElements": 15,
    "totalPages": 1,
    "currentPage": 0,
    "size": 20,
    "hasNext": false,
    "hasPrevious": false
  }
}
```

### GET `/api/search/products/new`
Lấy sản phẩm mới

**Query Parameters:**
- `page`: Số trang (default: 0)
- `size`: Số lượng per page (default: 20)

**Response:** Tương tự GET `/api/search/products/featured`

### GET `/api/search/products/bestsellers`
Lấy sản phẩm bán chạy

**Query Parameters:**
- `page`: Số trang (default: 0)
- `size`: Số lượng per page (default: 20)

**Response:** Tương tự GET `/api/search/products/featured`

### GET `/api/search/products/on-sale`
Lấy sản phẩm đang giảm giá

**Query Parameters:**
- `page`: Số trang (default: 0)
- `size`: Số lượng per page (default: 20)

**Response:** Tương tự GET `/api/search/products/featured`

---

## 💱 Currency APIs

### GET `/api/currency/available`
Lấy danh sách tiền tệ có sẵn

**Response:**
```json
{
  "success": true,
  "message": "Available currencies retrieved successfully",
  "data": [
    {
      "code": "VND",
      "name": "Vietnamese Dong",
      "symbol": "₫",
      "symbolPosition": "after",
      "decimalPlaces": 0,
      "isActive": true,
      "isDefault": true
    },
    {
      "code": "USD",
      "name": "US Dollar",
      "symbol": "$",
      "symbolPosition": "before",
      "decimalPlaces": 2,
      "isActive": true,
      "isDefault": false
    },
    {
      "code": "EUR",
      "name": "Euro",
      "symbol": "€",
      "symbolPosition": "after",
      "decimalPlaces": 2,
      "isActive": true,
      "isDefault": false
    }
  ]
}
```

### GET `/api/currency/current`
Lấy tiền tệ hiện tại

**Response:**
```json
{
  "success": true,
  "message": "Current currency retrieved successfully",
  "data": {
    "code": "VND",
    "name": "Vietnamese Dong",
    "symbol": "₫",
    "symbolPosition": "after",
    "decimalPlaces": 0,
    "isActive": true,
    "isDefault": true
  }
}
```

### POST `/api/currency/set/{currencyCode}`
Thay đổi tiền tệ hiện tại

**Response:**
```json
{
  "success": true,
  "message": "Currency changed successfully",
  "data": "USD"
}
```

### GET `/api/currency/convert`
Chuyển đổi tiền tệ

**Query Parameters:**
- `amount`: Số tiền cần chuyển đổi
- `fromCurrency`: Tiền tệ nguồn
- `toCurrency`: Tiền tệ đích

**Response:**
```json
{
  "success": true,
  "message": "Currency converted successfully",
  "data": {
    "fromCurrency": "VND",
    "toCurrency": "USD",
    "originalAmount": 50000000,
    "convertedAmount": 2000.00,
    "exchangeRate": 0.00004,
    "lastUpdated": "2024-01-01T10:00:00"
  }
}
```

### GET `/api/currency/format`
Định dạng giá tiền

**Query Parameters:**
- `amount`: Số tiền
- `currencyCode`: Mã tiền tệ

**Response:**
```json
{
  "success": true,
  "message": "Price formatted successfully",
  "data": "50,000,000 ₫"
}
```

### GET `/api/currency/rate`
Lấy tỷ giá hối đoái

**Query Parameters:**
- `fromCurrency`: Tiền tệ nguồn
- `toCurrency`: Tiền tệ đích

**Response:**
```json
{
  "success": true,
  "message": "Exchange rate retrieved successfully",
  "data": 0.00004
}
```

### POST `/api/currency/update-rates`
Cập nhật tỷ giá hối đoái

**Response:**
```json
{
  "success": true,
  "message": "Exchange rates updated successfully",
  "data": null
}
```

---

## 🌐 Language APIs

### GET `/api/language/available`
Lấy danh sách ngôn ngữ có sẵn

**Response:**
```json
{
  "success": true,
  "message": "Available languages retrieved successfully",
  "data": [
    {
      "code": "vi",
      "name": "Tiếng Việt",
      "nativeName": "Tiếng Việt",
      "isActive": true,
      "isDefault": true,
      "flag": "🇻🇳"
    },
    {
      "code": "en",
      "name": "English",
      "nativeName": "English",
      "isActive": true,
      "isDefault": false,
      "flag": "🇺🇸"
    }
  ]
}
```

### GET `/api/language/current`
Lấy ngôn ngữ hiện tại

**Response:**
```json
{
  "success": true,
  "message": "Current language retrieved successfully",
  "data": {
    "code": "vi",
    "name": "Tiếng Việt",
    "nativeName": "Tiếng Việt",
    "isActive": true,
    "isDefault": true,
    "flag": "🇻🇳"
  }
}
```

### POST `/api/language/set/{languageCode}`
Thay đổi ngôn ngữ hiện tại

**Response:**
```json
{
  "success": true,
  "message": "Language changed successfully",
  "data": "en"
}
```

### GET `/api/language/translations/{languageCode}`
Lấy tất cả bản dịch của ngôn ngữ

**Response:**
```json
{
  "success": true,
  "message": "Translations retrieved successfully",
  "data": {
    "home": "Trang chủ",
    "products": "Sản phẩm",
    "cart": "Giỏ hàng",
    "profile": "Hồ sơ",
    "login": "Đăng nhập",
    "register": "Đăng ký",
    "search": "Tìm kiếm",
    "categories": "Danh mục",
    "price": "Giá",
    "add_to_cart": "Thêm vào giỏ hàng",
    "buy_now": "Mua ngay"
  }
}
```

### GET `/api/language/translations/{languageCode}/{category}`
Lấy bản dịch theo danh mục

**Response:**
```json
{
  "success": true,
  "message": "Category translations retrieved successfully",
  "data": {
    "home": "Trang chủ",
    "products": "Sản phẩm",
    "categories": "Danh mục",
    "search": "Tìm kiếm"
  }
}
```

### GET `/api/language/translate/{key}`
Dịch một từ khóa

**Response:**
```json
{
  "success": true,
  "message": "Translation retrieved successfully",
  "data": "Trang chủ"
}
```

### GET `/api/language/translate/{key}/{languageCode}`
Dịch một từ khóa theo ngôn ngữ cụ thể

**Response:**
```json
{
  "success": true,
  "message": "Translation retrieved successfully",
  "data": "Home"
}
```

---

## 🔔 Notification APIs

### GET `/api/notifications`
Lấy danh sách thông báo

**Query Parameters:**
- `page`: Số trang (default: 0)
- `size`: Số lượng per page (default: 20)

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Đơn hàng đã được giao",
      "message": "Đơn hàng #ORD-20240101-001 của bạn đã được giao thành công",
      "type": "ORDER_DELIVERED",
      "isRead": false,
      "createdAt": "2024-01-03T14:30:00",
      "data": {
        "orderId": 1,
        "orderNumber": "ORD-20240101-001"
      }
    },
    {
      "id": 2,
      "title": "Giảm giá sản phẩm yêu thích",
      "message": "Bông tai kim cương trong danh sách yêu thích của bạn đã giảm giá từ 8,000,000₫ xuống 7,000,000₫",
      "type": "PRICE_DROP",
      "isRead": true,
      "createdAt": "2024-01-01T15:00:00",
      "data": {
        "productId": 3,
        "oldPrice": 8000000,
        "newPrice": 7000000
      }
    }
  ]
}
```

### GET `/api/notifications/unread`
Lấy thông báo chưa đọc

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Unread notifications retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Đơn hàng đã được giao",
      "message": "Đơn hàng #ORD-20240101-001 của bạn đã được giao thành công",
      "type": "ORDER_DELIVERED",
      "isRead": false,
      "createdAt": "2024-01-03T14:30:00",
      "data": {
        "orderId": 1,
        "orderNumber": "ORD-20240101-001"
      }
    }
  ]
}
```

### GET `/api/notifications/unread-count`
Lấy số lượng thông báo chưa đọc

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Unread count retrieved successfully",
  "data": 3
}
```

### PUT `/api/notifications/{notificationId}/read`
Đánh dấu thông báo đã đọc

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Notification marked as read successfully",
  "data": {
    "id": 1,
    "title": "Đơn hàng đã được giao",
    "message": "Đơn hàng #ORD-20240101-001 của bạn đã được giao thành công",
    "type": "ORDER_DELIVERED",
    "isRead": true,
    "createdAt": "2024-01-03T14:30:00",
    "readAt": "2024-01-03T16:00:00",
    "data": {
      "orderId": 1,
      "orderNumber": "ORD-20240101-001"
    }
  }
}
```

### PUT `/api/notifications/mark-all-read`
Đánh dấu tất cả thông báo đã đọc

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "All notifications marked as read successfully",
  "data": null
}
```

### GET `/api/notifications/preferences`
Lấy cài đặt thông báo

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "message": "Notification preferences retrieved successfully",
  "data": {
    "emailNotifications": {
      "orderUpdates": true,
      "priceDrops": true,
      "promotions": false,
      "newsletter": true
    },
    "pushNotifications": {
      "orderUpdates": true,
      "priceDrops": true,
      "promotions": true,
      "newsletter": false
    },
    "smsNotifications": {
      "orderUpdates": false,
      "priceDrops": false,
      "promotions": false,
      "newsletter": false
    }
  }
}
```

### PUT `/api/notifications/preferences`
Cập nhật cài đặt thông báo

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "emailNotifications": {
    "orderUpdates": true,
    "priceDrops": true,
    "promotions": true,
    "newsletter": false
  },
  "pushNotifications": {
    "orderUpdates": true,
    "priceDrops": false,
    "promotions": true,
    "newsletter": false
  },
  "smsNotifications": {
    "orderUpdates": false,
    "priceDrops": false,
    "promotions": false,
    "newsletter": false
  }
}
```

**Response:**
```json
{
  "success": true,
  "message": "Notification preferences updated successfully",
  "data": {
    "emailNotifications": {
      "orderUpdates": true,
      "priceDrops": true,
      "promotions": true,
      "newsletter": false
    },
    "pushNotifications": {
      "orderUpdates": true,
      "priceDrops": false,
      "promotions": true,
      "newsletter": false
    },
    "smsNotifications": {
      "orderUpdates": false,
      "priceDrops": false,
      "promotions": false,
      "newsletter": false
    }
  }
}
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
