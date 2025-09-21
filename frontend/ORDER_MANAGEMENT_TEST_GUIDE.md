# 📋 Order Management Test Guide

## ✅ **Chức năng đã hoàn thiện**

### **Backend APIs** (Spring Boot)
- ✅ `GET /api/admin/orders` - Lấy danh sách đơn hàng (có phân trang)
- ✅ `GET /api/admin/orders/{id}` - Lấy chi tiết đơn hàng
- ✅ `PUT /api/admin/orders/{id}/status` - Cập nhật trạng thái đơn hàng
- ✅ `PUT /api/admin/orders/{id}/cancel` - Hủy đơn hàng
- ✅ `GET /api/admin/orders/status/{status}` - Lấy đơn hàng theo trạng thái

### **Frontend UI** (Angular)
- ✅ **OrderManagementComponent** - Component quản lý đơn hàng
- ✅ **Statistics Dashboard** - Thống kê đơn hàng theo trạng thái
- ✅ **Order List Display** - Hiển thị danh sách đơn hàng
- ✅ **Search & Filter** - Tìm kiếm và lọc đơn hàng
- ✅ **Order Detail View** - Xem chi tiết đơn hàng
- ✅ **Status Management** - Cập nhật trạng thái đơn hàng
- ✅ **Order Cancellation** - Hủy đơn hàng
- ✅ **Pagination** - Phân trang danh sách
- ✅ **Responsive Design** - Giao diện thích ứng

## 🧪 **Test Cases**

### **1. Statistics Dashboard**
```
1. Truy cập Admin Panel → Đơn hàng
2. Kiểm tra 6 thẻ thống kê hiển thị:
   - Tổng đơn hàng
   - Chờ xử lý (PENDING)
   - Đang xử lý (PROCESSING)
   - Đã giao hàng (SHIPPED)
   - Đã nhận hàng (DELIVERED)
   - Đã hủy (CANCELLED)
3. Kiểm tra số liệu cập nhật real-time
```

### **2. Hiển thị danh sách đơn hàng**
```
1. Kiểm tra bảng đơn hàng hiển thị đầy đủ thông tin:
   - Mã đơn hàng
   - Thông tin khách hàng (Tên, Email, SĐT)
   - Trạng thái đơn hàng
   - Tổng tiền
   - Trạng thái thanh toán
   - Ngày tạo
   - Thao tác
2. Kiểm tra pagination hoạt động
```

### **3. Tìm kiếm đơn hàng**
```
1. Nhập từ khóa vào ô tìm kiếm
2. Kiểm tra: Chỉ hiển thị đơn hàng phù hợp
3. Test tìm kiếm theo: Mã đơn hàng, Tên khách hàng, Email, SĐT
4. Xóa từ khóa → Hiển thị tất cả đơn hàng
```

### **4. Lọc theo trạng thái**
```
1. Chọn trạng thái từ dropdown:
   - Tất cả
   - Chờ xử lý
   - Đang xử lý
   - Đã giao hàng
   - Đã nhận hàng
   - Đã hủy
2. Kiểm tra: Chỉ hiển thị đơn hàng có trạng thái tương ứng
3. Kiểm tra: Statistics cập nhật theo filter
```

### **5. Xem chi tiết đơn hàng**
```
1. Click nút "Xem chi tiết" (👁️) trên đơn hàng
2. Kiểm tra dialog hiển thị:
   - Thông tin đơn hàng (Mã, Trạng thái, Ngày tạo, Tổng tiền)
   - Thông tin khách hàng (Tên, Email, SĐT, Địa chỉ)
   - Danh sách sản phẩm (Tên, Số lượng, Đơn giá, Thành tiền)
   - Thông tin thanh toán (Phương thức, Trạng thái)
3. Click "Đóng" để thoát dialog
```

### **6. Cập nhật trạng thái đơn hàng**
```
1. Click nút "Cập nhật trạng thái" (✏️) trên đơn hàng
2. Chọn trạng thái mới từ dropdown:
   - PENDING: Chờ xử lý
   - PROCESSING: Đang xử lý
   - SHIPPED: Đã giao hàng
   - DELIVERED: Đã nhận hàng
   - CANCELLED: Đã hủy
3. Click "Cập nhật"
4. Kiểm tra: Trạng thái thay đổi trong bảng
5. Kiểm tra: Statistics cập nhật
```

### **7. Hủy đơn hàng**
```
1. Click nút "Hủy đơn hàng" (❌) trên đơn hàng
2. Nhập lý do hủy đơn hàng
3. Click "Hủy đơn hàng"
4. Kiểm tra: Trạng thái chuyển thành "Đã hủy"
5. Kiểm tra: Statistics cập nhật
```

### **8. Phân trang**
```
1. Kiểm tra pagination ở cuối bảng
2. Click "Next/Previous" để chuyển trang
3. Thay đổi số lượng hiển thị (10, 20, 50)
4. Kiểm tra: Dữ liệu load đúng theo trang
```

## 🎨 **UI/UX Features**

### **1. Statistics Cards**
- **Gradient backgrounds** cho từng loại trạng thái
- **Icons** phù hợp với từng trạng thái
- **Hover effects** với transform và shadow
- **Responsive grid** layout

### **2. Order Table**
- **Color-coded status badges**
- **Payment status indicators**
- **Action buttons** với tooltips
- **Hover effects** trên rows

### **3. Order Detail Dialog**
- **Large dialog** (900px width)
- **Organized sections** với icons
- **Product list** với grid layout
- **Payment information** display

### **4. Status Management**
- **Status dropdown** với tất cả options
- **Validation** cho required fields
- **Loading states** với spinners

### **5. Order Cancellation**
- **Reason textarea** với validation
- **Warning styling** cho cancel button
- **Confirmation** trước khi hủy

## 🔧 **Troubleshooting**

### **Lỗi thường gặp:**

#### **1. "Lỗi khi tải danh sách đơn hàng"**
- **Nguyên nhân**: Backend chưa chạy hoặc API không khả dụng
- **Giải pháp**: 
  ```bash
  cd trangsuc_js
  mvn spring-boot:run
  ```

#### **2. "403 Forbidden"**
- **Nguyên nhân**: Cần authentication với role ADMIN
- **Giải pháp**: 
  ```bash
  # Tạm thời disable security trong SecurityConfig.java
  .requestMatchers("/api/admin/**").permitAll()
  ```

#### **3. "Lỗi khi cập nhật trạng thái"**
- **Nguyên nhân**: Validation lỗi hoặc API không khả dụng
- **Giải pháp**: Kiểm tra backend logs và API endpoints

#### **4. "Không hiển thị danh sách"**
- **Nguyên nhân**: API trả về empty array hoặc lỗi
- **Giải pháp**: Kiểm tra console logs và network tab

#### **5. "Statistics không cập nhật"**
- **Nguyên nhân**: calculateStats() không được gọi
- **Giải pháp**: Kiểm tra method được gọi sau mỗi thao tác

## 📱 **Responsive Testing**

### **Desktop (1920x1080)**
- ✅ Statistics grid 6 columns
- ✅ Table hiển thị đầy đủ cột
- ✅ Dialog rộng 900px
- ✅ Pagination hoạt động tốt

### **Tablet (768x1024)**
- ✅ Statistics grid 3 columns
- ✅ Table cuộn ngang
- ✅ Dialog chiếm 95% màn hình
- ✅ Pagination responsive

### **Mobile (375x667)**
- ✅ Statistics grid 2 columns
- ✅ Table cuộn ngang
- ✅ Dialog fullscreen
- ✅ Font size nhỏ hơn

## 🚀 **Performance Testing**

### **Load Testing**
```
1. Tạo 100 đơn hàng
2. Kiểm tra thời gian load < 2s
3. Test search với 100 đơn hàng
4. Kiểm tra thời gian filter < 1s
5. Test statistics calculation < 500ms
```

### **Memory Testing**
```
1. Mở/đóng dialog nhiều lần
2. Kiểm tra không có memory leak
3. Test với nhiều đơn hàng (100+)
4. Test filter/search nhiều lần
```

## ✅ **Checklist hoàn thành**

- [x] **Backend APIs** - Đầy đủ CRUD operations
- [x] **Frontend Component** - OrderManagementComponent
- [x] **Statistics Dashboard** - 6 thẻ thống kê
- [x] **Order List Display** - Hiển thị đầy đủ thông tin
- [x] **Search & Filter** - Real-time search và status filter
- [x] **Order Detail View** - Dialog chi tiết đơn hàng
- [x] **Status Management** - Cập nhật trạng thái đơn hàng
- [x] **Order Cancellation** - Hủy đơn hàng với lý do
- [x] **Pagination** - Phân trang danh sách
- [x] **Error Handling** - User-friendly messages
- [x] **Responsive** - Mobile, tablet, desktop
- [x] **Integration** - Admin panel integration
- [x] **Documentation** - Complete test guide

## 🎯 **Kết quả mong đợi**

Sau khi hoàn thành test, bạn sẽ có:
- ✅ **Quản lý đơn hàng hoàn chỉnh** với CRUD operations
- ✅ **Statistics dashboard** trực quan
- ✅ **Giao diện đẹp mắt** và responsive
- ✅ **Tìm kiếm và lọc** nhanh chóng
- ✅ **Xem chi tiết** đơn hàng đầy đủ
- ✅ **Cập nhật trạng thái** linh hoạt
- ✅ **Hủy đơn hàng** với lý do
- ✅ **Phân trang** hiệu quả
- ✅ **Error handling** thân thiện
- ✅ **Integration** hoàn hảo với admin panel

## 🔗 **Related Files**

- `frontend/src/app/admin/order-management/order-management.ts`
- `frontend/src/app/admin/order-management/order-management.html`
- `frontend/src/app/admin/order-management/order-management.css`
- `frontend/src/app/services/admin.service.ts`
- `trangsuc_js/src/main/java/org/example/trangsuc_js/controller/admin/AdminController.java`
