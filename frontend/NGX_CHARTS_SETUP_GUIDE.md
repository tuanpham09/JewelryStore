# Hướng dẫn Setup ngx-charts cho Admin Dashboard

## ✅ **Đã hoàn thành**

### **1. Cài đặt thư viện**
```bash
cd frontend
npm install ngx-charts --legacy-peer-deps
```

### **2. Cập nhật Dashboard Component**

#### **Imports:**
- Thêm `NgxChartsModule` vào imports
- Import `@swimlane/ngx-charts`

#### **TypeScript Logic:**
- Thêm `revenueChartOptions` và `orderStatusChartOptions`
- Cập nhật `prepareChartData()` để format dữ liệu cho ngx-charts
- Dữ liệu biểu đồ được tạo từ API backend

#### **HTML Template:**
- **Biểu đồ doanh thu**: Sử dụng `ngx-charts-line-chart`
- **Biểu đồ trạng thái**: Sử dụng `ngx-charts-pie-chart`
- Responsive design với `[view]="[800, 400]"`

#### **CSS Styling:**
- Responsive cho mobile và tablet
- Styling cho ngx-charts components

### **3. Backend API Updates**

#### **AdminServiceImpl.java:**
- Thêm `generateRevenueChartData()`: Tạo dữ liệu doanh thu 7 ngày gần nhất
- Thêm `generateOrderStatusStats()`: Thống kê đơn hàng theo trạng thái
- Cập nhật `getDashboardStats()` để include chart data

#### **DTO Classes (Tách thành file riêng):**
- `TopProductDto.java`: productId, productName, productThumbnail, totalSold, totalRevenue
- `TopCustomerDto.java`: customerId, customerName, customerEmail, totalOrders, totalSpent
- `RevenueChartDto.java`: date, revenue, orders
- `OrderStatusDto.java`: status, count, totalValue
- Tất cả classes đã được tách thành file riêng biệt

## 🎯 **Cách test**

### **1. Khởi động Backend**
```bash
cd trangsuc_js
# Chạy Spring Boot application
```

### **2. Khởi động Frontend**
```bash
cd frontend
ng serve
```

### **3. Truy cập Admin Dashboard**
- URL: `http://localhost:4200/admin`
- Đăng nhập với tài khoản admin
- Kiểm tra 2 tab biểu đồ:
  - **Biểu đồ doanh thu theo thời gian**: Line chart
  - **Phân bố trạng thái đơn hàng**: Pie chart

## 📊 **Dữ liệu biểu đồ**

### **Revenue Chart (7 ngày gần nhất):**
```json
{
  "date": "2024-09-15",
  "revenue": 1500000.0,
  "orders": 5
}
```

### **Order Status Chart:**
```json
{
  "status": "PROCESSING",
  "count": 3,
  "totalValue": 1200000.0
}
```

## 🎨 **Tùy chỉnh biểu đồ**

### **Màu sắc:**
- Revenue chart: `['#d63384', '#ff6b9d', '#28a745', '#20c997', '#007bff']`
- Status chart: `['#d63384', '#ff6b9d', '#28a745', '#20c997', '#007bff', '#ffc107']`

### **Responsive:**
- Desktop: `[800, 400]`
- Mobile: `[300, 300]`

## 🔧 **Troubleshooting**

### **Lỗi thường gặp:**
1. **Module not found**: Kiểm tra `npm install ngx-charts`
2. **Chart không hiển thị**: Kiểm tra dữ liệu từ API
3. **Responsive issues**: Kiểm tra CSS media queries

### **Debug:**
```typescript
// Trong dashboard.ts
console.log('Revenue Chart Data:', this.revenueChartData);
console.log('Order Status Data:', this.orderStatusData);
```

## ✅ **Kết quả mong đợi**

1. **Line Chart**: Hiển thị xu hướng doanh thu 7 ngày
2. **Pie Chart**: Hiển thị phân bố trạng thái đơn hàng
3. **Responsive**: Hoạt động tốt trên mobile/tablet
4. **Real-time**: Dữ liệu được cập nhật từ database thực
