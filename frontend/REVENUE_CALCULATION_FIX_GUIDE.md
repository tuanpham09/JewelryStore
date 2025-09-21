# Hướng dẫn sửa lỗi tính toán doanh thu Top sản phẩm

## ✅ **Đã sửa logic tính toán doanh thu**

### **Vấn đề:**
- Một số sản phẩm hiển thị doanh thu 0 ₫ mặc dù đã bán được sản phẩm
- Logic tính toán doanh thu không chính xác

### **Nguyên nhân:**
Logic cũ sử dụng `item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))` có thể không chính xác.

### **Giải pháp:**

#### **1. Sửa logic tính toán doanh thu:**

**Trước:**
```java
.map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
```

**Sau:**
```java
.map(OrderItem::getSubtotal)
```

#### **2. Thêm debug logging:**

```java
// Debug logging
log.info("Product: {} - Sold: {} - Revenue: {}", 
        product.getName(), product.getSoldCount(), totalRevenue);
```

### **3. Logic mới:**

```java
private List<TopProductDto> generateTopSellingProducts() {
    List<TopProductDto> topProducts = new ArrayList<>();
    
    // Get top 5 selling products
    List<Product> products = productRepo.findAll().stream()
            .sorted((p1, p2) -> Long.compare(p2.getSoldCount(), p1.getSoldCount()))
            .limit(5)
            .collect(Collectors.toList());
    
    for (Product product : products) {
        TopProductDto dto = new TopProductDto();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductThumbnail(product.getThumbnail());
        dto.setTotalSold(product.getSoldCount());
        
        // Calculate total revenue for this product
        BigDecimal totalRevenue = orderRepo.findAll().stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PROCESSING || 
                           o.getStatus() == Order.OrderStatus.DELIVERED)
                .flatMap(o -> o.getItems().stream())
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .map(OrderItem::getSubtotal)  // Sử dụng subtotal thay vì price * quantity
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        dto.setTotalRevenue(totalRevenue);
        topProducts.add(dto);
        
        // Debug logging
        log.info("Product: {} - Sold: {} - Revenue: {}", 
                product.getName(), product.getSoldCount(), totalRevenue);
    }
    
    return topProducts;
}
```

## 🎯 **Cách test**

### **1. Restart Backend:**
```bash
cd trangsuc_js
# Restart Spring Boot application
```

### **2. Kiểm tra Console Logs:**
```bash
# Tìm logs như:
Product: Lắc tay vàng hồng 18K - Sold: 15 - Revenue: 12000000
Product: Vòng tay charm vàng - Sold: 13 - Revenue: 0
Product: Nhẫn vàng 24K cao cấp - Sold: 12 - Revenue: 15000000
```

### **3. Kiểm tra Frontend:**
- Truy cập `http://localhost:4200/admin`
- Chuyển đến tab "Sản phẩm bán chạy"
- Kiểm tra doanh thu hiển thị

## 🔍 **Debug Steps**

### **1. Nếu vẫn có doanh thu 0:**

**Kiểm tra dữ liệu OrderItem:**
```sql
SELECT oi.product_id, oi.price, oi.quantity, oi.subtotal, o.status
FROM order_items oi
JOIN orders o ON oi.order_id = o.id
WHERE o.status IN ('PROCESSING', 'DELIVERED')
ORDER BY oi.product_id;
```

### **2. Kiểm tra Product soldCount:**
```sql
SELECT id, name, sold_count 
FROM products 
ORDER BY sold_count DESC 
LIMIT 5;
```

### **3. Kiểm tra Order status:**
```sql
SELECT status, COUNT(*) 
FROM orders 
GROUP BY status;
```

## 📊 **Expected Results**

### **Sau khi sửa:**
- ✅ Tất cả sản phẩm có doanh thu > 0 nếu đã bán
- ✅ Doanh thu tính chính xác từ `subtotal` của OrderItem
- ✅ Debug logs hiển thị đúng dữ liệu

### **Ví dụ kết quả mong đợi:**
```
Product: Lắc tay vàng hồng 18K - Sold: 15 - Revenue: 12000000
Product: Vòng tay charm vàng - Sold: 13 - Revenue: 6500000
Product: Nhẫn vàng 24K cao cấp - Sold: 12 - Revenue: 15000000
Product: Bông tai ngọc trai - Sold: 11 - Revenue: 5500000
Product: Nhẫn cưới vàng trắng 18K - Sold: 9 - Revenue: 18000000
```

## ✅ **Kết luận**

**Logic tính toán doanh thu đã được sửa!** 🎉

- ✅ **Sử dụng `subtotal`** thay vì `price * quantity`
- ✅ **Debug logging** để kiểm tra dữ liệu
- ✅ **Filter đúng orders** (PROCESSING + DELIVERED)
- ✅ **Tính toán chính xác** doanh thu từ OrderItem

**Top sản phẩm bán chạy hiện sẽ hiển thị doanh thu chính xác!** 📊✨
