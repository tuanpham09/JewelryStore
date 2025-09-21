# 🗂️ Category Management Test Guide

## ✅ **Chức năng đã hoàn thiện**

### **Backend APIs** (Spring Boot)
- ✅ `GET /api/admin/categories` - Lấy danh sách danh mục
- ✅ `POST /api/admin/categories` - Tạo danh mục mới
- ✅ `PUT /api/admin/categories/{id}` - Cập nhật danh mục
- ✅ `DELETE /api/admin/categories/{id}` - Xóa danh mục

### **Frontend UI** (Angular)
- ✅ **CategoryManagementComponent** - Component quản lý danh mục
- ✅ **CRUD Operations** - Tạo, đọc, cập nhật, xóa danh mục
- ✅ **Search & Filter** - Tìm kiếm danh mục
- ✅ **Responsive Design** - Giao diện thích ứng
- ✅ **Form Validation** - Kiểm tra dữ liệu đầu vào
- ✅ **Auto Slug Generation** - Tự động tạo slug từ tên

## 🧪 **Test Cases**

### **1. Tạo danh mục mới**
```
1. Truy cập Admin Panel → Danh mục
2. Click "Thêm danh mục"
3. Nhập thông tin:
   - Tên: "Nhẫn vàng"
   - Mô tả: "Danh mục nhẫn vàng cao cấp"
   - URL hình ảnh: "https://example.com/ring.jpg"
   - Thứ tự: 1
4. Click "Thêm mới"
5. Kiểm tra: Danh mục xuất hiện trong bảng
```

### **2. Chỉnh sửa danh mục**
```
1. Click nút "Edit" (✏️) trên danh mục
2. Sửa thông tin:
   - Tên: "Nhẫn vàng cao cấp"
   - Mô tả: "Danh mục nhẫn vàng cao cấp, thiết kế tinh tế"
3. Click "Cập nhật"
4. Kiểm tra: Thông tin được cập nhật
```

### **3. Xóa danh mục**
```
1. Click nút "Delete" (🗑️) trên danh mục
2. Xác nhận xóa trong dialog
3. Kiểm tra: Danh mục biến mất khỏi bảng
```

### **4. Tìm kiếm danh mục**
```
1. Nhập từ khóa vào ô tìm kiếm
2. Kiểm tra: Chỉ hiển thị danh mục phù hợp
3. Xóa từ khóa → Hiển thị tất cả danh mục
```

### **5. Auto Slug Generation**
```
1. Tạo/chỉnh sửa danh mục
2. Nhập tên: "Nhẫn vàng 24K"
3. Kiểm tra: Slug tự động = "nhan-vang-24k"
4. Có thể chỉnh sửa slug thủ công
```

### **6. Trạng thái danh mục**
```
1. Tạo danh mục với "Danh mục hoạt động" = false
2. Kiểm tra: Hiển thị "Tạm dừng" trong bảng
3. Chỉnh sửa thành "Hoạt động"
4. Kiểm tra: Hiển thị "Hoạt động" trong bảng
```

## 🔧 **Troubleshooting**

### **Lỗi thường gặp:**

#### **1. "Lỗi khi tải danh mục"**
- **Nguyên nhân**: Backend chưa chạy hoặc API không khả dụng
- **Giải pháp**: 
  ```bash
  cd trangsuc_js
  mvn spring-boot:run
  ```

#### **2. "Lỗi khi thêm danh mục"**
- **Nguyên nhân**: Validation lỗi hoặc tên trùng
- **Giải pháp**: Kiểm tra tên danh mục không trùng, điền đầy đủ thông tin

#### **3. "Lỗi khi cập nhật danh mục"**
- **Nguyên nhân**: ID không tồn tại hoặc dữ liệu không hợp lệ
- **Giải pháp**: Refresh trang và thử lại

#### **4. "Lỗi khi xóa danh mục"**
- **Nguyên nhân**: Danh mục đang được sử dụng bởi sản phẩm
- **Giải pháp**: Xóa tất cả sản phẩm trong danh mục trước

## 📱 **Responsive Testing**

### **Desktop (1920x1080)**
- ✅ Bảng hiển thị đầy đủ cột
- ✅ Dialog form rộng 600px
- ✅ Controls nằm ngang

### **Tablet (768x1024)**
- ✅ Bảng cuộn ngang
- ✅ Dialog chiếm 95% màn hình
- ✅ Controls xếp dọc

### **Mobile (375x667)**
- ✅ Bảng cuộn ngang
- ✅ Dialog fullscreen
- ✅ Font size nhỏ hơn

## 🚀 **Performance Testing**

### **Load Testing**
```
1. Tạo 100 danh mục
2. Kiểm tra thời gian load < 2s
3. Test search với 100 danh mục
4. Kiểm tra thời gian filter < 1s
```

### **Memory Testing**
```
1. Mở/đóng dialog nhiều lần
2. Kiểm tra không có memory leak
3. Test với nhiều danh mục (100+)
```

## ✅ **Checklist hoàn thành**

- [x] **Backend APIs** - Đầy đủ CRUD operations
- [x] **Frontend Component** - CategoryManagementComponent
- [x] **UI/UX Design** - Modern, responsive
- [x] **Form Validation** - Client-side validation
- [x] **Error Handling** - User-friendly messages
- [x] **Search & Filter** - Real-time search
- [x] **Auto Slug** - Vietnamese character support
- [x] **Responsive** - Mobile, tablet, desktop
- [x] **Integration** - Admin panel integration
- [x] **Documentation** - Complete test guide

## 🎯 **Kết quả mong đợi**

Sau khi hoàn thành test, bạn sẽ có:
- ✅ **Quản lý danh mục hoàn chỉnh** với CRUD operations
- ✅ **Giao diện đẹp mắt** và responsive
- ✅ **Tìm kiếm nhanh chóng** và chính xác
- ✅ **Auto slug generation** hỗ trợ tiếng Việt
- ✅ **Form validation** đầy đủ
- ✅ **Error handling** thân thiện
- ✅ **Integration** hoàn hảo với admin panel

## 🔗 **Related Files**

- `frontend/src/app/admin/category-management/category-management.ts`
- `frontend/src/app/admin/category-management/category-management.html`
- `frontend/src/app/admin/category-management/category-management.css`
- `frontend/src/app/services/admin.service.ts`
- `trangsuc_js/src/main/java/org/example/trangsuc_js/controller/admin/AdminController.java`
