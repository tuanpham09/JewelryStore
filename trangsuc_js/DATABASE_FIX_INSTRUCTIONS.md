# 🔧 Database Fix Instructions

## ❌ **Vấn đề hiện tại**
Lỗi: `Data truncation: Out of range value for column 'original_price' at row 1`

**Nguyên nhân**: Cột `original_price` trong database có `DECIMAL(10,2)` - chỉ hỗ trợ tối đa 8 chữ số trước dấu phẩy, nhưng giá trị `100000000` có 9 chữ số.

## ✅ **Giải pháp**

### **Bước 1: Chạy SQL Script**
```bash
# Mở Command Prompt hoặc PowerShell
cd C:\Users\Admin\Documents\JewelryStore\trangsuc_js

# Chạy file batch (sẽ yêu cầu nhập password MySQL)
run_sql.bat
```

### **Bước 2: Hoặc chạy thủ công**
```sql
-- Kết nối MySQL và chạy các lệnh sau:
USE jewelry;

ALTER TABLE products MODIFY COLUMN original_price DECIMAL(15,2);
ALTER TABLE products MODIFY COLUMN sale_price DECIMAL(15,2);
ALTER TABLE products MODIFY COLUMN price DECIMAL(15,2);
```

### **Bước 3: Kiểm tra kết quả**
```sql
-- Kiểm tra cấu trúc bảng
DESCRIBE products;
```

## 🚀 **Sau khi sửa database**

1. **Restart Spring Boot application**
2. **Test lại API** với giá trị lớn
3. **Upload ảnh** sẽ hoạt động bình thường

## 📋 **Các thay đổi đã thực hiện**

### **Frontend (Angular)**:
- ✅ Sửa logic upload ảnh khi edit sản phẩm
- ✅ Phân biệt ảnh cũ và ảnh mới
- ✅ Thêm logging để debug
- ✅ Cải thiện error handling

### **Backend (Spring Boot)**:
- ✅ Cập nhật Entity Product với precision = 15, scale = 2
- ✅ Tạo SQL script để sửa database schema

## 🎯 **Kết quả mong đợi**
- ✅ Có thể lưu giá trị `originalPrice` lớn (100,000,000+)
- ✅ Upload ảnh hoạt động khi tạo mới và chỉnh sửa sản phẩm
- ✅ Hiển thị ảnh cũ và ảnh mới đúng cách
- ✅ Không còn lỗi "Data truncation"
