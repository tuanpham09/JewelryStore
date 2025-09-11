# 💎 JewelryStore

**JewelryStore** là hệ thống **bán trang sức trực tuyến** được phát triển với **Spring Boot (Back-end)** và **Angular (Front-end)** theo mô hình **MVC (Model – View – Controller)**.  
Hệ thống cung cấp đầy đủ chức năng thương mại điện tử: từ tìm kiếm, giỏ hàng, thanh toán, đến quản lý sản phẩm, quản lý kho, và phân quyền người dùng.  

---

## 🏗️ Công nghệ sử dụng

- **Back-end**:  
  - Java 17 + Spring Boot 3.x  
  - Spring Data JPA (ORM với MySQL)  
  - Spring Security + JWT (xác thực & phân quyền)  
  - Maven (quản lý dependency – `pom.xml`)  
  - MySQL 8.x (Database chính)  

- **Front-end**:  
  - Angular 16+  
  - TypeScript  
  - HTML5, SCSS, Bootstrap 5 / Angular Material  
  - Angular HttpClient (gọi API REST)  

- **Khác**:  
  - RESTful API  
  - JWT Authentication  
  - Redis (caching, tối ưu hiệu năng – mở rộng)  
  - Docker (triển khai dễ dàng)  

---

## 🔑 Chức năng hệ thống

### 👤 Người dùng (Customer Features)
- Đăng ký, đăng nhập, đăng xuất (JWT Authentication)  
- Cập nhật thông tin cá nhân  
- Tìm kiếm & lọc sản phẩm theo: tên, loại, giá, chất liệu (vàng, bạc, kim cương…)  
- Xem chi tiết sản phẩm (mô tả, hình ảnh, giá, khuyến mãi)  
- Thêm vào giỏ hàng, cập nhật số lượng, xóa sản phẩm  
- Wishlist (sản phẩm yêu thích, thông báo khi giảm giá)  
- Thanh toán đơn hàng (COD, thẻ tín dụng, PayPal, VNPay, Momo…)  
- Xem lịch sử mua hàng + trạng thái đơn hàng (chờ xử lý, vận chuyển, giao thành công, hủy)  
- Đánh giá sản phẩm (text, sao, kèm ảnh/video)  
- Nhận thông báo Email / SMS / Push Notification khi có đơn hàng hoặc khuyến mãi  
- Hỗ trợ đa ngôn ngữ (EN, VI…) và đa tiền tệ (VNĐ, USD, EUR…)  

---

### 🛠️ Quản trị viên (Admin Features)
- Quản lý sản phẩm: thêm, sửa, xóa, upload hình ảnh  
- Quản lý danh mục sản phẩm (nhẫn, vòng, dây chuyền…)  
- Quản lý người dùng: khóa / mở tài khoản, phân quyền (User / Staff / Admin)  
- Quản lý đơn hàng: xác nhận, cập nhật trạng thái, hủy đơn  
- Quản lý kho hàng (Inventory Management): cảnh báo khi sắp hết hàng  
- Quản lý khuyến mãi: Flash Sale, Combo giảm giá, Coupon / Voucher  
- Quản lý vận chuyển: tích hợp API giao hàng (GHTK, GHN, Viettel Post…)  
- Dashboard thống kê trực quan:  
  - Doanh thu theo ngày / tháng / năm  
  - Top sản phẩm bán chạy  
  - Khách hàng thân thiết nhất  
- Xuất báo cáo PDF / Excel  

---

### 🛡️ Hệ thống (System-Level Features)
- Xác thực 2 lớp (2FA / OTP SMS/Email)  
- Quản lý phân quyền chi tiết (Role-based Access Control – RBAC)  
- Cache sản phẩm & giỏ hàng với Redis → tối ưu tốc độ  
- Tích hợp CI/CD (Jenkins / GitHub Actions)  
- Triển khai trên Cloud (AWS, GCP, Azure)  
- Hỗ trợ Docker + Kubernetes để mở rộng hệ thống  
- Chatbot AI hỗ trợ khách hàng 24/7  
- Affiliate / Referral Program (giới thiệu bạn bè, nhận ưu đãi)  

---

## 📂 Cấu trúc dự án (MVC)

```

JewelryStore/
│── backend/ (Spring Boot)
│   ├── controller/   # REST Controllers (API endpoints)
│   ├── model/        # Entities (User, Product, Order, Cart...)
│   ├── dto/          # Data Transfer Objects
│   ├── repository/   # DAO Layer (JPA Repositories)
│   ├── service/      # Business Logic
│   └── security/     # JWT & Spring Security
│   └── application.properties
│   └── pom.xml
│
│── frontend/ (Angular)
│   ├── src/app/
│   │   ├── components/   # View Components (UI)
│   │   ├── services/     # API Calls
│   │   ├── models/       # Interfaces/DTO
│   │   └── guards/       # Route Guards (Auth, Admin)
│   └── package.json

````

---

## 🗄️ Cấu hình Database (MySQL)

File `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jewelrystore?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
````

---

## 🚀 Cách chạy dự án

### Back-end (Spring Boot)

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Server chạy tại: `http://localhost:8080/api`

### Front-end (Angular)

```bash
cd frontend
npm install
ng serve --open
```

UI chạy tại: `http://localhost:4200`

---

## 📌 Một số API chính

* **Đăng ký User** → `POST /api/auth/register`
* **Đăng nhập User** → `POST /api/auth/login`
* **Danh sách sản phẩm** → `GET /api/products`
* **Chi tiết sản phẩm** → `GET /api/products/{id}`
* **Thêm vào giỏ hàng** → `POST /api/cart/add`
* **Thanh toán đơn hàng** → `POST /api/orders/checkout`
* **Lịch sử mua hàng** → `GET /api/orders/history`

---

## 📖 Hướng phát triển tương lai

* AI Recommendation: gợi ý sản phẩm phù hợp cho khách hàng
* Thử trang sức bằng AR (Augmented Reality)
* Loyalty Program (tích điểm, hạng thành viên)
* Triển khai Microservices khi mở rộng quy mô

---

✨ JewelryStore – Giúp khách hàng tìm được món trang sức hoàn hảo 💍
