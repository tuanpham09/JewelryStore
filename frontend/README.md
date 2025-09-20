# Frontend - JewelryStore

Frontend cho ứng dụng JewelryStore được xây dựng bằng Angular 20.

## Cài đặt

### Yêu cầu hệ thống
- Node.js 18.0.0 trở lên
- npm 8.0.0 trở lên

### Cài đặt dependencies
```bash
cd frontend
npm install
```

## Chạy ứng dụng

### Chạy trong môi trường development
```bash
npm start
# hoặc
ng serve --proxy-config proxy.conf.json
```

Ứng dụng sẽ chạy tại địa chỉ http://localhost:4200 và tự động chuyển tiếp các API request đến backend.

### Chạy với auto-open browser
```bash
npm run start:dev
# hoặc
ng serve --proxy-config proxy.conf.json --open
```

### Chạy với cổng khác
```bash
ng serve --proxy-config proxy.conf.json --port 4300
```

## Kết nối với Backend

Frontend sử dụng Angular Proxy để chuyển tiếp các API request đến backend. Cấu hình proxy được đặt trong file `proxy.conf.json`:

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

Với cấu hình này, mọi request đến `/api/*` sẽ được chuyển tiếp đến `http://localhost:8080/api/*`. Trong auth.service.ts, chúng ta sử dụng đường dẫn tương đối:

```typescript
// Sử dụng đường dẫn tương đối để hoạt động với proxy
private apiUrl = '/api/auth';
```

Nếu backend của bạn chạy ở địa chỉ khác, hãy cập nhật lại trong file `proxy.conf.json`.

## Các chức năng hiện có

### Xác thực
- Đăng nhập: `/login`
- Đăng ký: `/register`

### Luồng xác thực
1. Người dùng đăng ký tài khoản mới
2. Người dùng đăng nhập và nhận JWT token
3. Token được lưu trong localStorage
4. Mọi request sau đó sẽ tự động gửi kèm token trong header Authorization

## Cấu trúc dự án

```
frontend/
├── src/
│   ├── app/
│   │   ├── login/              # Component đăng nhập
│   │   ├── register/           # Component đăng ký
│   │   ├── auth.service.ts     # Service xử lý xác thực
│   │   ├── auth.interceptor.ts # HTTP Interceptor để gửi token
│   │   ├── app.ts              # Component chính
│   │   ├── app.routes.ts       # Cấu hình routing
│   │   └── app.config.ts       # Cấu hình ứng dụng
│   ├── assets/                 # Static assets
│   ├── index.html              # HTML entry point
│   └── main.ts                 # Entry point
├── angular.json                # Cấu hình Angular
└── package.json                # Dependencies
```

## Xử lý lỗi thường gặp

### CORS Error
Nếu bạn gặp lỗi CORS, hãy đảm bảo backend đã được cấu hình để chấp nhận request từ frontend:
```java
// CorsConfig.java
registry.addMapping("/**")
        .allowedOrigins("http://localhost:4200", "http://localhost:4300")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
```

### 401 Unauthorized
Nếu bạn gặp lỗi 401, có thể token đã hết hạn hoặc không hợp lệ. Hãy đăng xuất và đăng nhập lại.

### Không thể kết nối đến API
Đảm bảo backend đang chạy và cổng 8080 đang mở. Kiểm tra bằng cách truy cập trực tiếp vào một API endpoint, ví dụ: http://localhost:8080/api/auth/login.

## Build cho production

```bash
npm run build
# hoặc
ng build --prod
```

Các file build sẽ được tạo trong thư mục `dist/`.

## Testing

### Unit tests
```bash
npm test
# hoặc
ng test
```

### End-to-end tests
```bash
npm run e2e
# hoặc
ng e2e
```

## Tài liệu tham khảo

- [Angular Documentation](https://angular.dev/docs)
- [Angular Material](https://material.angular.io/)
- [RxJS](https://rxjs.dev/)