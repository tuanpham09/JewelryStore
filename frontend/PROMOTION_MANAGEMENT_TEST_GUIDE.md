# 🎁 Promotion Management Test Guide

## ✅ **Chức năng đã hoàn thiện**

### **Backend APIs** (Spring Boot)
- ✅ `GET /api/admin/promotions` - Lấy danh sách khuyến mãi
- ✅ `POST /api/admin/promotions` - Tạo khuyến mãi mới
- ✅ `PUT /api/admin/promotions/{id}` - Cập nhật khuyến mãi
- ✅ `DELETE /api/admin/promotions/{id}` - Xóa khuyến mãi
- ✅ `PUT /api/admin/promotions/{id}/toggle` - Bật/tắt khuyến mãi

### **Frontend UI** (Angular)
- ✅ **PromotionManagementComponent** - Component quản lý khuyến mãi
- ✅ **Statistics Dashboard** - Thống kê khuyến mãi theo trạng thái
- ✅ **Promotion List Display** - Hiển thị danh sách khuyến mãi
- ✅ **Search & Filter** - Tìm kiếm và lọc khuyến mãi
- ✅ **Promotion Detail View** - Xem chi tiết khuyến mãi
- ✅ **Create/Edit Promotion** - Tạo và chỉnh sửa khuyến mãi
- ✅ **Status Management** - Bật/tắt khuyến mãi
- ✅ **Responsive Design** - Giao diện thích ứng

## 🧪 **Test Cases**

### **1. Statistics Dashboard**
```
1. Truy cập Admin Panel → Khuyến mãi
2. Kiểm tra 5 thẻ thống kê hiển thị:
   - Tổng khuyến mãi
   - Đang hoạt động (ACTIVE)
   - Không hoạt động (INACTIVE)
   - Hết hạn (EXPIRED)
   - Sắp diễn ra (UPCOMING)
3. Kiểm tra số liệu cập nhật real-time
```

### **2. Hiển thị danh sách khuyến mãi**
```
1. Kiểm tra bảng khuyến mãi hiển thị đầy đủ thông tin:
   - Tên khuyến mãi & Mô tả
   - Loại khuyến mãi
   - Giá trị khuyến mãi
   - Mã khuyến mãi
   - Trạng thái
   - Thống kê sử dụng
   - Thời gian áp dụng
   - Thao tác
2. Kiểm tra hiển thị đúng format cho từng loại khuyến mãi
```

### **3. Tìm kiếm khuyến mãi**
```
1. Nhập từ khóa vào ô tìm kiếm
2. Kiểm tra: Chỉ hiển thị khuyến mãi phù hợp
3. Test tìm kiếm theo: Tên, Mô tả, Mã khuyến mãi
4. Xóa từ khóa → Hiển thị tất cả khuyến mãi
```

### **4. Lọc theo trạng thái**
```
1. Chọn trạng thái từ dropdown:
   - Tất cả
   - Hoạt động
   - Không hoạt động
   - Hết hạn
   - Sắp diễn ra
2. Kiểm tra: Chỉ hiển thị khuyến mãi có trạng thái tương ứng
3. Kiểm tra: Statistics cập nhật theo filter
```

### **5. Tạo khuyến mãi mới**
```
1. Click nút "Tạo khuyến mãi mới"
2. Điền thông tin cơ bản:
   - Tên khuyến mãi (required)
   - Mã khuyến mãi (required) - có thể generate tự động
   - Mô tả
3. Cài đặt khuyến mãi:
   - Loại khuyến mãi: PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING
   - Giá trị khuyến mãi
   - Đơn hàng tối thiểu
   - Giới hạn sử dụng
4. Thời gian áp dụng:
   - Ngày bắt đầu
   - Ngày kết thúc
5. Sản phẩm áp dụng:
   - Tất cả sản phẩm
   - Theo danh mục
   - Theo sản phẩm cụ thể
6. Trạng thái: Kích hoạt/không kích hoạt
7. Click "Tạo"
8. Kiểm tra: Khuyến mãi xuất hiện trong danh sách
```

### **6. Chỉnh sửa khuyến mãi**
```
1. Click nút "Chỉnh sửa" (✏️) trên khuyến mãi
2. Cập nhật thông tin cần thiết
3. Click "Cập nhật"
4. Kiểm tra: Thông tin được cập nhật trong bảng
```

### **7. Xem chi tiết khuyến mãi**
```
1. Click nút "Xem chi tiết" (👁️) trên khuyến mãi
2. Kiểm tra dialog hiển thị:
   - Thông tin cơ bản
   - Cài đặt khuyến mãi
   - Thống kê sử dụng
   - Thời gian áp dụng
   - Sản phẩm áp dụng
3. Click "Đóng" để thoát dialog
```

### **8. Bật/tắt khuyến mãi**
```
1. Click nút "Bật/tắt" (▶️/⏸️) trên khuyến mãi
2. Kiểm tra: Trạng thái thay đổi trong bảng
3. Kiểm tra: Statistics cập nhật
```

### **9. Xóa khuyến mãi**
```
1. Click nút "Xóa" (🗑️) trên khuyến mãi
2. Xác nhận xóa trong dialog
3. Kiểm tra: Khuyến mãi biến mất khỏi bảng
4. Kiểm tra: Statistics cập nhật
```

## 🎨 **UI/UX Features**

### **1. Statistics Cards**
- **Gradient backgrounds** cho từng loại trạng thái
- **Icons** phù hợp với từng trạng thái
- **Hover effects** với transform và shadow
- **Responsive grid** layout

### **2. Promotion Table**
- **Color-coded status badges**
- **Usage progress bars**
- **Type badges** cho loại khuyến mãi
- **Date range display** với icons
- **Action buttons** với tooltips

### **3. Promotion Form**
- **Multi-section form** với icons
- **Date pickers** cho thời gian
- **Dynamic fields** theo loại khuyến mãi
- **Code generator** cho mã khuyến mãi
- **Validation** đầy đủ

### **4. Promotion Detail**
- **Comprehensive information** display
- **Usage statistics** với progress bars
- **Date range** information
- **Applicable products** details

### **5. Status Management**
- **Smart status detection** (ACTIVE, INACTIVE, EXPIRED, UPCOMING)
- **Visual indicators** với màu sắc
- **Toggle functionality** dễ dàng

## 🔧 **Troubleshooting**

### **Lỗi thường gặp:**

#### **1. "Lỗi khi tải danh sách khuyến mãi"**
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

#### **3. "Lỗi khi tạo/cập nhật khuyến mãi"**
- **Nguyên nhân**: Validation lỗi hoặc API không khả dụng
- **Giải pháp**: Kiểm tra backend logs và API endpoints

#### **4. "Không hiển thị danh sách"**
- **Nguyên nhân**: API trả về empty array hoặc lỗi
- **Giải pháp**: Kiểm tra console logs và network tab

#### **5. "Statistics không cập nhật"**
- **Nguyên nhân**: calculateStats() không được gọi
- **Giải pháp**: Kiểm tra method được gọi sau mỗi thao tác

#### **6. "Date picker không hoạt động"**
- **Nguyên nhân**: MatDatepickerModule chưa được import
- **Giải pháp**: Kiểm tra imports trong component

## 📱 **Responsive Testing**

### **Desktop (1920x1080)**
- ✅ Statistics grid 5 columns
- ✅ Table hiển thị đầy đủ cột
- ✅ Dialog rộng 900px
- ✅ Form layout 2 columns

### **Tablet (768x1024)**
- ✅ Statistics grid 3 columns
- ✅ Table cuộn ngang
- ✅ Dialog chiếm 95% màn hình
- ✅ Form layout 1 column

### **Mobile (375x667)**
- ✅ Statistics grid 2 columns
- ✅ Table cuộn ngang
- ✅ Dialog fullscreen
- ✅ Form layout 1 column

## 🚀 **Performance Testing**

### **Load Testing**
```
1. Tạo 100 khuyến mãi
2. Kiểm tra thời gian load < 2s
3. Test search với 100 khuyến mãi
4. Kiểm tra thời gian filter < 1s
5. Test statistics calculation < 500ms
```

### **Memory Testing**
```
1. Mở/đóng dialog nhiều lần
2. Kiểm tra không có memory leak
3. Test với nhiều khuyến mãi (100+)
4. Test filter/search nhiều lần
```

## ✅ **Checklist hoàn thành**

- [x] **Backend APIs** - Đầy đủ CRUD operations
- [x] **Frontend Component** - PromotionManagementComponent
- [x] **Statistics Dashboard** - 5 thẻ thống kê
- [x] **Promotion List Display** - Hiển thị đầy đủ thông tin
- [x] **Search & Filter** - Real-time search và status filter
- [x] **Promotion Detail View** - Dialog chi tiết khuyến mãi
- [x] **Create/Edit Promotion** - Form tạo và chỉnh sửa
- [x] **Status Management** - Bật/tắt khuyến mãi
- [x] **Code Generator** - Tự động tạo mã khuyến mãi
- [x] **Date Pickers** - Chọn thời gian áp dụng
- [x] **Usage Statistics** - Thống kê sử dụng với progress bar
- [x] **Error Handling** - User-friendly messages
- [x] **Responsive** - Mobile, tablet, desktop
- [x] **Integration** - Admin panel integration
- [x] **Documentation** - Complete test guide

## 🎯 **Kết quả mong đợi**

Sau khi hoàn thành test, bạn sẽ có:
- ✅ **Quản lý khuyến mãi hoàn chỉnh** với CRUD operations
- ✅ **Statistics dashboard** trực quan
- ✅ **Giao diện đẹp mắt** và responsive
- ✅ **Tìm kiếm và lọc** nhanh chóng
- ✅ **Xem chi tiết** khuyến mãi đầy đủ
- ✅ **Tạo/chỉnh sửa** khuyến mãi linh hoạt
- ✅ **Bật/tắt khuyến mãi** dễ dàng
- ✅ **Code generator** tiện lợi
- ✅ **Date pickers** thân thiện
- ✅ **Usage statistics** trực quan
- ✅ **Error handling** thân thiện
- ✅ **Integration** hoàn hảo với admin panel

## 🔗 **Related Files**

- `frontend/src/app/admin/promotion-management/promotion-management.ts`
- `frontend/src/app/admin/promotion-management/promotion-management.html`
- `frontend/src/app/admin/promotion-management/promotion-management.css`
- `frontend/src/app/services/admin.service.ts`
- `trangsuc_js/src/main/java/org/example/trangsuc_js/controller/admin/AdminController.java`
