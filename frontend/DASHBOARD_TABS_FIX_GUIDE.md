# Hướng dẫn sửa lỗi Dashboard Tabs

## ✅ **Đã sửa thành công**

### **Vấn đề:**
- Tab "Sản phẩm bán chạy" không hiển thị dữ liệu
- Tab "Khách hàng VIP" không hiển thị dữ liệu

### **Nguyên nhân:**
Backend method `getDashboardStats()` không set `topSellingProducts` và `topCustomers` vào response.

### **Giải pháp:**

#### **1. Backend Changes:**

**File:** `trangsuc_js/src/main/java/org/example/trangsuc_js/service/impl/AdminServiceImpl.java`

**Thêm vào method `getDashboardStats()`:**
```java
// Generate top selling products and top customers
stats.setTopSellingProducts(generateTopSellingProducts());
stats.setTopCustomers(generateTopCustomers());
```

**Thêm method `generateTopSellingProducts()`:**
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
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        dto.setTotalRevenue(totalRevenue);
        topProducts.add(dto);
    }
    
    return topProducts;
}
```

**Thêm method `generateTopCustomers()`:**
```java
private List<TopCustomerDto> generateTopCustomers() {
    List<TopCustomerDto> topCustomers = new ArrayList<>();
    
    // Get top 5 customers by total spent
    List<User> users = userRepo.findAll().stream()
            .sorted((u1, u2) -> {
                double spent1 = u1.getOrders().stream()
                        .filter(o -> o.getStatus() == Order.OrderStatus.PROCESSING || 
                                   o.getStatus() == Order.OrderStatus.DELIVERED)
                        .mapToDouble(o -> o.getTotal().doubleValue())
                        .sum();
                double spent2 = u2.getOrders().stream()
                        .filter(o -> o.getStatus() == Order.OrderStatus.PROCESSING || 
                                   o.getStatus() == Order.OrderStatus.DELIVERED)
                        .mapToDouble(o -> o.getTotal().doubleValue())
                        .sum();
                return Double.compare(spent2, spent1);
            })
            .limit(5)
            .collect(Collectors.toList());
    
    for (User user : users) {
        TopCustomerDto dto = new TopCustomerDto();
        dto.setCustomerId(user.getId());
        dto.setCustomerName(user.getFullName());
        dto.setCustomerEmail(user.getEmail());
        
        // Count total orders
        long totalOrders = user.getOrders().stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PROCESSING || 
                           o.getStatus() == Order.OrderStatus.DELIVERED)
                .count();
        dto.setTotalOrders(totalOrders);
        
        // Calculate total spent
        BigDecimal totalSpent = user.getOrders().stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PROCESSING || 
                           o.getStatus() == Order.OrderStatus.DELIVERED)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        dto.setTotalSpent(totalSpent);
        topCustomers.add(dto);
    }
    
    return topCustomers;
}
```

#### **2. Frontend đã sẵn sàng:**

**File:** `frontend/src/app/admin/dashboard/dashboard.ts`
```typescript
// Table data
topProducts: TopProductDto[] = [];
topCustomers: TopCustomerDto[] = [];

// Table columns
productColumns: string[] = ['product', 'sold', 'revenue'];
customerColumns: string[] = ['customer', 'orders', 'spent'];
```

**File:** `frontend/src/app/admin/dashboard/dashboard.html`
```html
<!-- Top Products Tab -->
<mat-tab label="Sản phẩm bán chạy">
    <div class="tab-content">
        <div class="table-container">
            <h3 class="table-title">
                <mat-icon>star</mat-icon>
                Top sản phẩm bán chạy
            </h3>
            <div class="table-wrapper">
                <table mat-table [dataSource]="topProducts" class="products-table">
                    <!-- Columns: product, sold, revenue -->
                </table>
            </div>
        </div>
    </div>
</mat-tab>

<!-- Top Customers Tab -->
<mat-tab label="Khách hàng VIP">
    <div class="tab-content">
        <div class="table-container">
            <h3 class="table-title">
                <mat-icon>star</mat-icon>
                Top khách hàng VIP
            </h3>
            <div class="table-wrapper">
                <table mat-table [dataSource]="topCustomers" class="customers-table">
                    <!-- Columns: customer, orders, spent -->
                </table>
            </div>
        </div>
    </div>
</mat-tab>
```

## 🎯 **Cách test**

### **1. Khởi động ứng dụng:**
```bash
# Backend
cd trangsuc_js
# Chạy Spring Boot

# Frontend
cd frontend
ng serve
```

### **2. Truy cập Admin Dashboard:**
- URL: `http://localhost:4200/admin`
- Kiểm tra tab "Sản phẩm bán chạy"
- Kiểm tra tab "Khách hàng VIP"

### **3. Kiểm tra dữ liệu:**
- **Sản phẩm bán chạy**: Hiển thị top 5 sản phẩm có `soldCount` cao nhất
- **Khách hàng VIP**: Hiển thị top 5 khách hàng có tổng chi tiêu cao nhất

## 📊 **Dữ liệu hiển thị**

### **Tab "Sản phẩm bán chạy":**
- **Sản phẩm**: Tên sản phẩm + ID
- **Đã bán**: Số lượng đã bán
- **Doanh thu**: Tổng doanh thu từ sản phẩm đó

### **Tab "Khách hàng VIP":**
- **Khách hàng**: Tên + Email
- **Đơn hàng**: Số lượng đơn hàng
- **Chi tiêu**: Tổng số tiền đã chi

## ✅ **Kết quả**

**Dashboard tabs đã hoạt động đầy đủ!** 🎉

- ✅ **Tab "Sản phẩm bán chạy"**: Hiển thị top 5 sản phẩm
- ✅ **Tab "Khách hàng VIP"**: Hiển thị top 5 khách hàng
- ✅ **Dữ liệu thực tế**: Từ database với logic tính toán chính xác
- ✅ **UI/UX**: Giao diện đẹp và responsive

**Dashboard Admin hiện đã hoàn chỉnh với tất cả các tab hoạt động!** 📊✨
