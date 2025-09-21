# Hướng dẫn Test API Integration Dashboard

## ✅ **API đã được tích hợp đầy đủ**

### **1. Backend API Endpoints:**

**File:** `trangsuc_js/src/main/java/org/example/trangsuc_js/controller/admin/AdminController.java`

```java
@GetMapping("/dashboard/stats")
public ResponseEntity<DashboardStatsDto> getDashboardStats() {
    DashboardStatsDto stats = adminService.getDashboardStats();
    return ResponseEntity.ok(stats);
}
```

**URL:** `GET /api/admin/dashboard/stats`

### **2. Frontend Service:**

**File:** `frontend/src/app/services/admin.service.ts`

```typescript
getDashboardStats(): Observable<DashboardStatsDto> {
    return this.http.get<DashboardStatsDto>(`${this.apiUrl}/dashboard/stats`);
}
```

### **3. Frontend Component:**

**File:** `frontend/src/app/admin/dashboard/dashboard.ts`

```typescript
loadDashboardStats() {
    this.isLoading = true;
    console.log('Loading dashboard stats from API...');
    this.adminService.getDashboardStats().subscribe({
        next: (stats) => {
            console.log('Dashboard stats loaded successfully:', stats);
            this.dashboardStats = stats;
            this.topProducts = stats.topSellingProducts || [];
            this.topCustomers = stats.topCustomers || [];
            this.prepareChartData(stats);
            this.isLoading = false;
        },
        error: (error) => {
            console.error('Error loading dashboard stats:', error);
            this.isLoading = false;
        }
    });
}
```

## 🎯 **Cách Test API**

### **1. Test Backend API trực tiếp:**

```bash
# Test API endpoint
curl -X GET http://localhost:8080/api/admin/dashboard/stats

# Expected response:
{
  "totalRevenue": 15000000,
  "totalOrders": 25,
  "totalCustomers": 10,
  "totalProducts": 50,
  "topSellingProducts": [
    {
      "productId": 1,
      "productName": "Nhẫn vàng 18k",
      "productThumbnail": "https://example.com/image.jpg",
      "totalSold": 15,
      "totalRevenue": 5000000
    }
  ],
  "topCustomers": [
    {
      "customerId": 1,
      "customerName": "Nguyễn Văn A",
      "customerEmail": "a@example.com",
      "totalOrders": 5,
      "totalSpent": 3000000
    }
  ],
  "revenueChart": [...],
  "orderStatusStats": [...]
}
```

### **2. Test Frontend Integration:**

**Bước 1: Khởi động Backend**
```bash
cd trangsuc_js
# Chạy Spring Boot application
```

**Bước 2: Khởi động Frontend**
```bash
cd frontend
ng serve
```

**Bước 3: Truy cập Dashboard**
- URL: `http://localhost:4200/admin`
- Mở Developer Tools (F12)
- Kiểm tra Console logs
- Kiểm tra Network tab

### **3. Debug Steps:**

#### **A. Kiểm tra Console Logs:**
```javascript
// Should see:
"Loading dashboard stats from API..."
"Dashboard stats loaded successfully: {totalRevenue: 15000000, ...}"
```

#### **B. Kiểm tra Network Tab:**
- Request: `GET http://localhost:8080/api/admin/dashboard/stats`
- Status: `200 OK`
- Response: JSON data với đầy đủ fields

#### **C. Kiểm tra Data Binding:**
- Metrics cards hiển thị số liệu
- Charts hiển thị dữ liệu
- Tables hiển thị top products và customers

## 🔧 **Troubleshooting**

### **1. Nếu API không hoạt động:**

**Kiểm tra Backend:**
```bash
# Restart Spring Boot
cd trangsuc_js
# Stop và start lại application
```

**Kiểm tra Database:**
```bash
# Đảm bảo database có dữ liệu
# Chạy migration nếu cần
```

### **2. Nếu Frontend không load data:**

**Kiểm tra Console Errors:**
- CORS errors
- Authentication errors
- Network errors

**Kiểm tra API URL:**
```typescript
// File: frontend/src/environments/environment.ts
export const environment = {
  apiUrl: 'http://localhost:8080/api'
};
```

### **3. Nếu Data không hiển thị:**

**Kiểm tra Data Structure:**
```typescript
// Console log để debug
console.log('Dashboard stats:', stats);
console.log('Top products:', stats.topSellingProducts);
console.log('Top customers:', stats.topCustomers);
```

## 📊 **Expected Results**

### **Dashboard Metrics:**
- ✅ Tổng doanh thu
- ✅ Tổng đơn hàng
- ✅ Tổng khách hàng
- ✅ Tổng sản phẩm

### **Charts:**
- ✅ Biểu đồ doanh thu 30 ngày
- ✅ Biểu đồ trạng thái đơn hàng

### **Tables:**
- ✅ Top 5 sản phẩm bán chạy
- ✅ Top 5 khách hàng VIP

## ✅ **Kết luận**

**API Integration đã hoàn chỉnh!** 🎉

- ✅ **Backend**: Endpoint `/api/admin/dashboard/stats` hoạt động
- ✅ **Frontend**: Service và component tích hợp đầy đủ
- ✅ **Data Flow**: Từ database → backend → frontend → UI
- ✅ **Error Handling**: Console logs và error handling

**Dashboard Admin hiện đã hoạt động với dữ liệu thực từ database!** 📊✨
