# 🚀 Hướng dẫn cài đặt và chạy dự án JewelryStore

## 📋 Yêu cầu hệ thống

### Phần mềm cần thiết:
- **Java 17** hoặc cao hơn
- **Node.js 18+** và **npm**
- **MySQL 8.0+**
- **Maven 3.6+**
- **Git**

### Kiểm tra phiên bản:
```bash
java -version
node -v
npm -v
mysql --version
mvn -version
```

---

## 🗄️ Cài đặt Database

### 1. Cài đặt MySQL
- Tải và cài đặt MySQL từ: https://dev.mysql.com/downloads/mysql/
- Hoặc sử dụng XAMPP/WAMP (bao gồm MySQL)

### 2. Tạo Database
```sql
-- Kết nối MySQL với tài khoản root
mysql -u root -p

-- Tạo database
CREATE DATABASE jewelry;

-- Tạo user (tùy chọn)
CREATE USER 'jewelry_user'@'localhost' IDENTIFIED BY '110803';
GRANT ALL PRIVILEGES ON jewelry.* TO 'jewelry_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Cấu hình Database
Chỉnh sửa file `trangsuc_js/src/main/resources/application.properties`:
```properties
# Thay đổi thông tin kết nối nếu cần
spring.datasource.url=jdbc:mysql://localhost:3307/jewelry?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=110803
```

---

## 🔧 Cài đặt Backend (Spring Boot)

### 1. Di chuyển vào thư mục backend
```bash
cd trangsuc_js
```

### 2. Cài đặt dependencies
```bash
# Sử dụng Maven wrapper (khuyến nghị)
./mvnw clean install

# Hoặc sử dụng Maven global
mvn clean install
```

### 3. Chạy ứng dụng
```bash
# Sử dụng Maven wrapper
./mvnw spring-boot:run

# Hoặc sử dụng Maven global
mvn spring-boot:run

# Hoặc chạy JAR file
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### 4. Kiểm tra Backend
- Backend sẽ chạy tại: `http://localhost:8080`
- API Documentation: `http://localhost:8080/api`
- Health Check: `http://localhost:8080/actuator/health`

---

## 🎨 Cài đặt Frontend (Angular)

### 1. Di chuyển vào thư mục frontend
```bash
cd frontend
```

### 2. Cài đặt dependencies
```bash
npm install
```

### 3. Chạy ứng dụng
```bash
# Chạy development server
npm start
# hoặc
ng serve

# Chạy với auto-open browser
ng serve --open

# Chạy trên port khác
ng serve --port 4300
```

### 4. Kiểm tra Frontend
- Frontend sẽ chạy tại: `http://localhost:4200`
- Ứng dụng sẽ tự động mở trong browser

---

## 🔄 Chạy toàn bộ dự án

### Cách 1: Chạy tuần tự
```bash
# Terminal 1 - Backend
cd trangsuc_js
./mvnw spring-boot:run

# Terminal 2 - Frontend  
cd frontend
npm start
```

### Cách 2: Sử dụng script (tùy chọn)
Tạo file `start.bat` (Windows) hoặc `start.sh` (Linux/Mac):

**start.bat (Windows):**
```batch
@echo off
echo Starting JewelryStore...
start "Backend" cmd /k "cd trangsuc_js && mvnw spring-boot:run"
timeout /t 10
start "Frontend" cmd /k "cd frontend && npm start"
```

**start.sh (Linux/Mac):**
```bash
#!/bin/bash
echo "Starting JewelryStore..."
gnome-terminal -- bash -c "cd trangsuc_js && ./mvnw spring-boot:run; exec bash"
sleep 10
gnome-terminal -- bash -c "cd frontend && npm start; exec bash"
```

---

## 🧪 Kiểm tra hoạt động

### 1. Kiểm tra Backend API
```bash
# Test health endpoint
curl http://localhost:8080/actuator/health

# Test products endpoint
curl http://localhost:8080/api/products
```

### 2. Kiểm tra Frontend
- Mở browser: `http://localhost:4200`
- Kiểm tra console không có lỗi
- Test các chức năng cơ bản

### 3. Kiểm tra kết nối Frontend-Backend
- Mở Developer Tools (F12)
- Kiểm tra Network tab khi thực hiện các thao tác
- Đảm bảo API calls thành công

---

## 🐛 Xử lý lỗi thường gặp

### Lỗi Database Connection
```
Error: Could not create connection to database server
```
**Giải pháp:**
- Kiểm tra MySQL đã chạy chưa
- Kiểm tra thông tin kết nối trong `application.properties`
- Kiểm tra port MySQL (mặc định 3306, dự án dùng 3307)

### Lỗi Port đã được sử dụng
```
Error: Port 8080 was already in use
```
**Giải pháp:**
```bash
# Tìm process đang sử dụng port
netstat -ano | findstr :8080

# Kill process (Windows)
taskkill /PID <PID> /F

# Hoặc thay đổi port trong application.properties
server.port=8081
```

### Lỗi Node modules
```
Error: Cannot find module
```
**Giải pháp:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Lỗi CORS
```
Access to XMLHttpRequest has been blocked by CORS policy
```
**Giải pháp:**
- Kiểm tra cấu hình CORS trong `CorsConfig.java`
- Đảm bảo frontend chạy trên port được phép (4200, 4300)

---

## 📁 Cấu trúc dự án

```
JewelryStore/
├── trangsuc_js/                 # Backend (Spring Boot)
│   ├── src/main/java/org/example/trangsuc_js/
│   │   ├── config/              # Cấu hình (Security, CORS)
│   │   ├── controller/          # REST Controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── entity/              # JPA Entities
│   │   ├── repository/          # JPA Repositories
│   │   ├── service/             # Business Logic
│   │   └── util/                # Utilities
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/                    # Frontend (Angular)
│   ├── src/app/
│   │   ├── app.config.ts
│   │   ├── app.routes.ts
│   │   └── app.ts
│   ├── src/
│   │   ├── index.html
│   │   └── main.ts
│   └── package.json
└── README.md
```

---

## 🔧 Cấu hình nâng cao

### 1. Thay đổi Database
Để sử dụng PostgreSQL thay vì MySQL:
```xml
<!-- Thêm vào pom.xml -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

```properties
# Cập nhật application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jewelry
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 2. Cấu hình JWT
```properties
# Thay đổi secret key mạnh hơn
jwt.secret=YourVeryLongAndSecureSecretKeyHere123456789
jwt.expiration=86400000  # 24 giờ
```

### 3. Cấu hình Logging
```properties
# Thêm vào application.properties
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 📞 Hỗ trợ

Nếu gặp vấn đề trong quá trình cài đặt:

1. **Kiểm tra logs:**
   - Backend: Console output hoặc `logs/application.log`
   - Frontend: Browser Console (F12)

2. **Kiểm tra phiên bản:**
   - Đảm bảo tất cả phần mềm đúng phiên bản yêu cầu

3. **Kiểm tra network:**
   - Firewall không chặn port 8080, 4200
   - Antivirus không chặn ứng dụng

4. **Reset hoàn toàn:**
   ```bash
   # Backend
   cd trangsuc_js
   ./mvnw clean
   
   # Frontend
   cd frontend
   rm -rf node_modules
   npm install
   ```

---

## 🎯 Bước tiếp theo

Sau khi chạy thành công:

1. **Tạo tài khoản admin** đầu tiên
2. **Thêm dữ liệu mẫu** (categories, products)
3. **Test các chức năng** cơ bản
4. **Tùy chỉnh giao diện** theo nhu cầu
5. **Deploy lên server** khi sẵn sàng

---

✨ **Chúc bạn thành công với dự án JewelryStore!** 💎
