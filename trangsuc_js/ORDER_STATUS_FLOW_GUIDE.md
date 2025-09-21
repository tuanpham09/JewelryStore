# Hướng dẫn Flow Trạng thái Đơn hàng và Thống kê Admin

## 🔄 **Flow hoàn chỉnh từ tạo order đến thống kê**

### **1. Tạo Order**
```
Status: PENDING (Chờ thanh toán)
Payment Status: PENDING (Chờ thanh toán)
Payment Method: PAYOS
Payment Reference: null (sẽ được set sau)
Revenue: 0 (chưa thanh toán)
```

### **2. Tạo Payment với PAYOS**
```
Order: PENDING → Vẫn PENDING
Payment Status: PENDING → Vẫn PENDING
Payment Reference: null → orderCode (từ PAYOS)
Payment: PENDING
Revenue: 0 (chưa thanh toán)
```

### **3. Thanh toán thành công (PAYOS callback)**
```
Order: PENDING → PROCESSING (Đã thanh toán, đang xử lý)
Order.payment_status: PENDING → "SUCCESS"
Order.payment_method: PAYOS → Vẫn PAYOS
Order.payment_reference: orderCode → Vẫn orderCode (không đổi)
Payment: PENDING → SUCCESS
Revenue: ✅ Được tính vào thống kê
```

### **4. Admin cập nhật trạng thái**
```
PROCESSING → SHIPPED (Đã gửi hàng)
SHIPPED → DELIVERED (Đã giao hàng)
```

## 📊 **Thống kê Admin Dashboard**

### **Total Revenue**
- ✅ **Tính cả PROCESSING và DELIVERED orders**
- ✅ **Bao gồm tất cả đơn hàng đã thanh toán**

### **Order Status Breakdown**
- `PENDING`: Chờ thanh toán
- `PROCESSING`: Đã thanh toán, đang xử lý ✅ **Revenue được tính**
- `SHIPPED`: Đã gửi hàng ✅ **Revenue được tính**
- `DELIVERED`: Đã giao hàng ✅ **Revenue được tính**
- `CANCELLED`: Đã hủy
- `REFUNDED`: Đã hoàn tiền

## 🎯 **Logic Thống kê**

### **Revenue Calculation**
```java
// Trước đây: Chỉ tính DELIVERED
.filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)

// Bây giờ: Tính cả PROCESSING và DELIVERED
.filter(o -> o.getStatus() == Order.OrderStatus.PROCESSING || 
           o.getStatus() == Order.OrderStatus.DELIVERED)
```

### **Order Status Flow**
```
PENDING → PROCESSING → SHIPPED → DELIVERED
   ↓           ↓           ↓         ↓
 0 VND    ✅ Revenue  ✅ Revenue  ✅ Revenue
```

## 🧪 **Test Flow**

### **1. Tạo Order**
```bash
curl -X POST http://localhost:8080/api/payment/create-order \
  -H "Content-Type: application/json" \
  -d '{"shippingName": "Test", "shippingPhone": "0123456789", "shippingAddress": "123 Test", "items": [{"productId": 1, "quantity": 1}]}'
```

**Expected:**
- Order status: `PENDING`
- Revenue: 0

### **2. Tạo Payment**
```bash
curl -X POST http://localhost:8080/api/payment/create-payment/1
```

**Expected:**
- Order status: `PENDING` (chưa thay đổi)
- Payment status: `PENDING`

### **3. Confirm Payment**
```bash
curl -X POST http://localhost:8080/api/payment/confirm-payment \
  -H "Content-Type: application/json" \
  -d '{"orderCode": "1001234", "paymentId": "test_payment_id"}'
```

**Expected:**
- Order status: `PROCESSING` ✅
- Payment status: `SUCCESS` ✅
- Revenue: ✅ **Được tính vào thống kê**

### **4. Kiểm tra Admin Dashboard**
```bash
curl -X GET http://localhost:8080/api/admin/dashboard/stats
```

**Expected:**
- `totalRevenue`: > 0 ✅
- `totalOrders`: > 0 ✅
- `pendingOrders`: 0 (vì đã PROCESSING)

## 📋 **Database Schema**

### **orders table**
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    order_number VARCHAR(255), -- orderCode từ PAYOS
    status VARCHAR(50), -- PENDING, PROCESSING, SHIPPED, DELIVERED (trạng thái đơn hàng)
    total_amount DECIMAL(15,2),
    paid_at TIMESTAMP, -- Khi thanh toán thành công
    payment_status VARCHAR(50), -- PENDING, SUCCESS, FAILED (trạng thái thanh toán)
    payment_method VARCHAR(50), -- PAYOS, CASH, BANK_TRANSFER
    payment_reference VARCHAR(255), -- orderCode từ PAYOS
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### **payments table**
```sql
CREATE TABLE payments (
    id BIGINT PRIMARY KEY,
    order_id BIGINT,
    status VARCHAR(50), -- PENDING, SUCCESS, FAILED
    amount DECIMAL(15,2),
    paid_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## ✅ **Kết quả**

1. **Thanh toán thành công** → Order status = `PROCESSING`
2. **Admin thống kê** → Tính revenue từ `PROCESSING` và `DELIVERED` orders
3. **Revenue hiển thị đúng** → Không bị mất do status khác nhau

## 🔍 **Debug Logs**

Khi chạy, kiểm tra logs:
```
Creating order: PENDING
Creating payment: PENDING
Confirming payment: orderCode=1001234
Found order: id=1, orderNumber=1001234, status=PENDING
Updated order status: PROCESSING
Payment confirmed: SUCCESS
Admin dashboard: totalRevenue=2000 (từ PROCESSING order)
```

**Bây giờ Admin Dashboard sẽ hiển thị đúng revenue từ các đơn hàng đã thanh toán!** 🎉✨
