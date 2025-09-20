# 🔗 API Integration Guide - Home Page

## 📋 Tổng quan

Trang Home đã được tích hợp với API backend để hiển thị danh sách sản phẩm thực từ database thay vì dữ liệu mock.

## 🛠️ Các thay đổi đã thực hiện

### 1. **ProductService** (`src/app/services/product.service.ts`)
- ✅ Tạo service mới để gọi API sản phẩm
- ✅ Interface `Product` phù hợp với API response
- ✅ Các method: `getAllProducts()`, `getProductById()`, `getProductsByCategory()`, etc.

### 2. **CategoryService** (`src/app/services/category.service.ts`)
- ✅ Tạo service mới để gọi API danh mục
- ✅ Interface `Category` phù hợp với API response
- ✅ Các method: `getAllCategories()`, `getCategoryById()`

### 3. **Home Component** (`src/app/home/home.ts`)
- ✅ Import `ProductService`, `CategoryService` và các interface
- ✅ Thêm loading state (`isLoading`, `isLoadingCategories`) và error handling
- ✅ Method `loadProducts()` và `loadCategories()` để gọi API
- ✅ Cập nhật `filteredProducts` getter để filter theo category từ API
- ✅ Cập nhật `navigateToProduct()` để sử dụng `number` thay vì `string`
- ✅ Fallback categories nếu API thất bại

### 4. **Template HTML** (`src/app/home/home.html`)
- ✅ Thêm loading spinner với `mat-spinner`
- ✅ Thêm error state với retry button
- ✅ Thêm loading state cho categories
- ✅ Cập nhật product card để hiển thị:
  - `product.thumbnail` thay vì `product.image`
  - `product.categoryName` thay vì `product.category`
  - Sale badges (`isOnSale`, `isNew`)
  - Rating stars và review count
  - Sale price vs original price
- ✅ Thêm "No products found" state

### 5. **CSS Styles** (`src/app/home/home.css`)
- ✅ Loading container styles
- ✅ Error container styles
- ✅ No products state styles
- ✅ Category loading styles
- ✅ Product badges (sale, new)
- ✅ Price container với sale price
- ✅ Rating stars styles

## 🔌 API Endpoints được sử dụng

### GET `/api/categories`
- **Mục đích**: Lấy danh sách danh mục sản phẩm
- **Response**: 
```json
{
  "success": true,
  "message": "Categories retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Nhẫn",
      "slug": "nhan",
      "description": "Danh mục nhẫn vàng, bạc...",
      "imageUrl": "https://example.com/category.jpg",
      "isActive": true,
      "sortOrder": 1
    }
  ]
}
```

### GET `/api/products`
- **Mục đích**: Lấy tất cả sản phẩm
- **Response**: 
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Nhẫn vàng 18k",
      "slug": "nhan-vang-18k",
      "description": "Nhẫn vàng 18k đẹp...",
      "price": 5000000,
      "stock": 10,
      "thumbnail": "https://example.com/image.jpg",
      "categoryName": "Nhẫn",
      "averageRating": 4.5,
      "reviewCount": 12
    }
  ]
}
```

## 🎯 Tính năng mới

### 1. **Loading State**
- Hiển thị spinner khi đang tải dữ liệu sản phẩm
- Hiển thị spinner khi đang tải danh mục
- Message "Đang tải sản phẩm..." và "Đang tải danh mục..."

### 2. **Error Handling**
- Hiển thị error message khi API call thất bại
- Button "Thử lại" để retry API call
- Fallback categories nếu API categories thất bại

### 3. **Dynamic Categories**
- Danh mục được load từ API thay vì hardcode
- Hiển thị loading state khi đang tải danh mục
- Fallback categories nếu API thất bại
- Filter sản phẩm theo danh mục được chọn

### 4. **Product Badges**
- Badge "GIẢM GIÁ" cho sản phẩm đang sale
- Badge "MỚI" cho sản phẩm mới

### 5. **Enhanced Product Display**
- Hiển thị category name
- Rating stars với review count
- Sale price vs original price
- Fallback image khi không có thumbnail

### 6. **No Products State**
- Hiển thị khi không có sản phẩm phù hợp với filter
- Button để reset filter

## 🔧 Cách sử dụng

### 1. **Khởi động Backend**
```bash
cd trangsuc_js
mvn spring-boot:run
```

### 2. **Khởi động Frontend**
```bash
cd frontend
npm start
```

### 3. **Truy cập trang Home**
- URL: `http://localhost:4200`
- Trang sẽ tự động gọi API `GET /api/categories` và `GET /api/products`
- Hiển thị loading state cho cả categories và products
- Hiển thị danh mục và sản phẩm hoặc error state

## 🐛 Troubleshooting

### 1. **API không kết nối được**
- Kiểm tra backend có chạy trên port 8080 không
- Kiểm tra CORS configuration
- Kiểm tra network tab trong DevTools

### 2. **Dữ liệu không hiển thị**
- Kiểm tra API response format
- Kiểm tra console logs
- Kiểm tra `environment.ts` có đúng API URL không

### 3. **Loading state không biến mất**
- Kiểm tra API response có `success: true` không
- Kiểm tra error handling logic

## 📝 Notes

- **API Base URL**: Được lấy từ `environment.ts` (`http://localhost:8080/api`)
- **Error Handling**: Tất cả API calls đều có error handling
- **Loading States**: User experience được cải thiện với loading indicators
- **Responsive**: Tất cả states đều responsive trên mobile

## 🚀 Next Steps

1. **Tích hợp Cart API** - Thêm sản phẩm vào giỏ hàng
2. **Tích hợp Search API** - Tìm kiếm sản phẩm
3. **Tích hợp Wishlist API** - Thêm vào danh sách yêu thích
4. **Tích hợp Product Detail API** - Chi tiết sản phẩm
5. **Tích hợp Review API** - Đánh giá sản phẩm
