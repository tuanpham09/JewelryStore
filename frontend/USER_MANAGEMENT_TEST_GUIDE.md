# 👥 User Management Test Guide

## ✅ **Chức năng đã hoàn thiện**

### **Backend APIs** (Spring Boot)
- ✅ `GET /api/admin/users` - Lấy danh sách người dùng (có phân trang)
- ✅ `GET /api/admin/users/{id}` - Lấy thông tin người dùng theo ID
- ✅ `PUT /api/admin/users/{id}/status` - Cập nhật trạng thái người dùng
- ✅ `PUT /api/admin/users/{id}/roles` - Cập nhật quyền người dùng
- ✅ `DELETE /api/admin/users/{id}` - Xóa người dùng

### **Frontend UI** (Angular)
- ✅ **UserManagementComponent** - Component quản lý người dùng
- ✅ **User List Display** - Hiển thị danh sách người dùng
- ✅ **Search & Filter** - Tìm kiếm người dùng
- ✅ **Role Management** - Quản lý quyền người dùng
- ✅ **Status Toggle** - Bật/tắt tài khoản
- ✅ **Pagination** - Phân trang danh sách
- ✅ **Responsive Design** - Giao diện thích ứng

## 🧪 **Test Cases**

### **1. Hiển thị danh sách người dùng**
```
1. Truy cập Admin Panel → Người dùng
2. Kiểm tra danh sách người dùng hiển thị
3. Kiểm tra thông tin: Tên, Email, Quyền, Trạng thái, Ngày tạo
4. Kiểm tra pagination hoạt động
```

### **2. Tìm kiếm người dùng**
```
1. Nhập từ khóa vào ô tìm kiếm
2. Kiểm tra: Chỉ hiển thị người dùng phù hợp
3. Test tìm kiếm theo: Tên, Email, Số điện thoại
4. Xóa từ khóa → Hiển thị tất cả người dùng
```

### **3. Chỉnh sửa quyền người dùng**
```
1. Click nút "Edit" (✏️) trên người dùng
2. Chọn/quỷ chọn quyền:
   - ROLE_USER: Người dùng
   - ROLE_STAFF: Nhân viên  
   - ROLE_ADMIN: Quản trị viên
3. Click "Cập nhật"
4. Kiểm tra: Quyền được cập nhật trong bảng
```

### **4. Bật/tắt tài khoản**
```
1. Click nút "Lock/Unlock" (🔒/🔓) trên người dùng
2. Kiểm tra: Trạng thái thay đổi trong bảng
3. Kiểm tra: Thông báo thành công hiển thị
```

### **5. Xóa người dùng**
```
1. Click nút "Delete" (🗑️) trên người dùng
2. Xác nhận xóa trong dialog
3. Kiểm tra: Người dùng biến mất khỏi bảng
4. Kiểm tra: Thông báo xóa thành công
```

### **6. Phân trang**
```
1. Kiểm tra pagination ở cuối bảng
2. Click "Next/Previous" để chuyển trang
3. Thay đổi số lượng hiển thị (10, 20, 50)
4. Kiểm tra: Dữ liệu load đúng theo trang
```

## 🔧 **Troubleshooting**

### **Lỗi thường gặp:**

#### **1. "Lỗi khi tải danh sách người dùng"**
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

#### **3. "Lỗi khi cập nhật quyền"**
- **Nguyên nhân**: Validation lỗi hoặc API không khả dụng
- **Giải pháp**: Kiểm tra backend logs và API endpoints

#### **4. "Không hiển thị danh sách"**
- **Nguyên nhân**: API trả về empty array hoặc lỗi
- **Giải pháp**: Kiểm tra console logs và network tab

## 📱 **Responsive Testing**

### **Desktop (1920x1080)**
- ✅ Bảng hiển thị đầy đủ cột
- ✅ Dialog form rộng 600px
- ✅ Pagination hoạt động tốt

### **Tablet (768x1024)**
- ✅ Bảng cuộn ngang
- ✅ Dialog chiếm 95% màn hình
- ✅ Pagination responsive

### **Mobile (375x667)**
- ✅ Bảng cuộn ngang
- ✅ Dialog fullscreen
- ✅ Font size nhỏ hơn

## 🚀 **Performance Testing**

### **Load Testing**
```
1. Tạo 100 người dùng
2. Kiểm tra thời gian load < 2s
3. Test search với 100 người dùng
4. Kiểm tra thời gian filter < 1s
```

### **Memory Testing**
```
1. Mở/đóng dialog nhiều lần
2. Kiểm tra không có memory leak
3. Test với nhiều người dùng (100+)
```

## ✅ **Checklist hoàn thành**

- [x] **Backend APIs** - Đầy đủ CRUD operations
- [x] **Frontend Component** - UserManagementComponent
- [x] **UI/UX Design** - Modern, responsive
- [x] **User List Display** - Hiển thị đầy đủ thông tin
- [x] **Search & Filter** - Real-time search
- [x] **Role Management** - Quản lý quyền người dùng
- [x] **Status Toggle** - Bật/tắt tài khoản
- [x] **Pagination** - Phân trang danh sách
- [x] **Error Handling** - User-friendly messages
- [x] **Responsive** - Mobile, tablet, desktop
- [x] **Integration** - Admin panel integration
- [x] **Documentation** - Complete test guide

## 🎯 **Kết quả mong đợi**

Sau khi hoàn thành test, bạn sẽ có:
- ✅ **Quản lý người dùng hoàn chỉnh** với CRUD operations
- ✅ **Giao diện đẹp mắt** và responsive
- ✅ **Tìm kiếm nhanh chóng** và chính xác
- ✅ **Quản lý quyền** linh hoạt
- ✅ **Bật/tắt tài khoản** dễ dàng
- ✅ **Phân trang** hiệu quả
- ✅ **Error handling** thân thiện
- ✅ **Integration** hoàn hảo với admin panel

## 🔗 **Related Files**

- `frontend/src/app/admin/user-management/user-management.ts`
- `frontend/src/app/admin/user-management/user-management.html`
- `frontend/src/app/admin/user-management/user-management.css`
- `frontend/src/app/services/admin.service.ts`
- `trangsuc_js/src/main/java/org/example/trangsuc_js/controller/admin/AdminController.java`
