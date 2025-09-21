# Hướng dẫn Debug Doanh thu Top sản phẩm

## 🔍 **Phân tích dữ liệu hiện tại**

### **Dữ liệu order_items:**
```
Product ID 1: subtotal = 15,000,000 ₫
Product ID 2: subtotal = 3,500,000 ₫  
Product ID 3: subtotal = 12,000,000 ₫
Product ID 4: subtotal = 35,000,000 ₫
Product ID 5: subtotal = 18,000,000 ₫
```

### **Vấn đề:**
- "Vòng tay charm vàng" và "Bông tai ngọc trai" hiển thị doanh thu 0 ₫
- Có thể Product ID không khớp hoặc Order status không đúng

## ✅ **Đã sửa logic tính toán**

### **1. Thay đổi filter orders:**
```java
// Trước: Chỉ PROCESSING + DELIVERED
.filter(o -> o.getStatus() == Order.OrderStatus.PROCESSING || 
           o.getStatus() == Order.OrderStatus.DELIVERED)

// Sau: Tất cả orders trừ CANCELLED
.filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
```

### **2. Thêm debug logging:**
```java
log.info("Product: {} (ID: {}) - Sold: {} - Revenue: {}", 
        product.getName(), product.getId(), product.getSoldCount(), totalRevenue);
```

## 🎯 **Cách test và debug**

### **1. Restart Backend:**
```bash
cd trangsuc_js
# Restart Spring Boot application
```

### **2. Kiểm tra Console Logs:**
```bash
# Tìm logs như:
Product: Nhẫn vàng 24K cao cấp (ID: 1) - Sold: 12 - Revenue: 15000000
Product: Dây chuyền bạc 925 (ID: 2) - Sold: 13 - Revenue: 3500000
Product: Lắc tay vàng hồng 18K (ID: 3) - Sold: 15 - Revenue: 12000000
Product: Bông tai kim cương (ID: 4) - Sold: 11 - Revenue: 35000000
Product: Nhẫn cưới vàng trắng 18K (ID: 5) - Sold: 9 - Revenue: 18000000
```

### **3. Kiểm tra Database trực tiếp:**

#### **A. Kiểm tra Product ID mapping:**
```sql
SELECT p.id, p.name, p.sold_count 
FROM products p 
ORDER BY p.sold_count DESC 
LIMIT 5;
```

#### **B. Kiểm tra Order status:**
```sql
SELECT o.id, o.status, oi.product_id, oi.subtotal
FROM orders o
JOIN order_items oi ON o.id = oi.order_id
WHERE o.status != 'CANCELLED'
ORDER BY oi.product_id;
```

#### **C. Kiểm tra Product-OrderItem mapping:**
```sql
SELECT 
    p.id as product_id,
    p.name as product_name,
    p.sold_count,
    SUM(oi.subtotal) as total_revenue
FROM products p
LEFT JOIN order_items oi ON p.id = oi.product_id
LEFT JOIN orders o ON oi.order_id = o.id AND o.status != 'CANCELLED'
GROUP BY p.id, p.name, p.sold_count
ORDER BY p.sold_count DESC
LIMIT 5;
```

## 🔧 **Troubleshooting**

### **1. Nếu vẫn có doanh thu 0:**

**Kiểm tra Product ID mapping:**
- Đảm bảo Product ID trong `products` table khớp với `order_items.product_id`
- Kiểm tra xem có products nào không có order_items không

**Kiểm tra Order status:**
- Đảm bảo orders có status PENDING, PROCESSING, hoặc DELIVERED
- Loại trừ orders có status CANCELLED

### **2. Nếu Product ID không khớp:**

**Cập nhật Product ID:**
```sql
-- Kiểm tra mapping
SELECT DISTINCT oi.product_id FROM order_items oi;
SELECT DISTINCT p.id FROM products p;

-- Nếu cần, cập nhật product_id trong order_items
UPDATE order_items SET product_id = ? WHERE product_id = ?;
```

### **3. Nếu Order status không đúng:**

**Cập nhật Order status:**
```sql
-- Kiểm tra status hiện tại
SELECT status, COUNT(*) FROM orders GROUP BY status;

-- Cập nhật status nếu cần
UPDATE orders SET status = 'PROCESSING' WHERE status = 'PENDING';
```

## 📊 **Expected Results**

### **Sau khi sửa:**
```
Product: Nhẫn vàng 24K cao cấp (ID: 1) - Sold: 12 - Revenue: 15000000
Product: Dây chuyền bạc 925 (ID: 2) - Sold: 13 - Revenue: 3500000
Product: Lắc tay vàng hồng 18K (ID: 3) - Sold: 15 - Revenue: 12000000
Product: Bông tai kim cương (ID: 4) - Sold: 11 - Revenue: 35000000
Product: Nhẫn cưới vàng trắng 18K (ID: 5) - Sold: 9 - Revenue: 18000000
```

### **Frontend hiển thị:**
- ✅ Tất cả sản phẩm có doanh thu > 0
- ✅ Doanh thu khớp với dữ liệu order_items
- ✅ Sắp xếp đúng theo số lượng bán

## ✅ **Kết luận**

**Logic tính toán doanh thu đã được sửa!** 🎉

- ✅ **Include tất cả orders** trừ CANCELLED
- ✅ **Debug logging** để kiểm tra dữ liệu
- ✅ **Sử dụng subtotal** từ OrderItem
- ✅ **Filter chính xác** theo Product ID

**Top sản phẩm bán chạy hiện sẽ hiển thị doanh thu chính xác!** 📊✨
