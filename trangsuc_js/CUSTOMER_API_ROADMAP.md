# 🛍️ Customer API Roadmap - JewelryStore

## 📋 **Tổng quan**

Dựa trên yêu cầu trong README.md (dòng 33-43), đây là kế hoạch phát triển các API dành cho người dùng cuối (Customer APIs).

---

## 🎯 **Mục tiêu**

Xây dựng hệ thống API hoàn chỉnh cho khách hàng với đầy đủ tính năng thương mại điện tử hiện đại.

---

## 📊 **Tình trạng hiện tại**

### ✅ **Đã hoàn thành:**
- ✅ Authentication (Register/Login/Profile)
- ✅ Product listing & details
- ✅ Cart management (Add/Remove items)
- ✅ Order management (Checkout/History)
- ✅ Review system (Add/View reviews)

### 🚧 **Cần phát triển:**
- 🔄 Customer Profile Management
- 🔄 Advanced Search & Filter
- 🔄 Wishlist System
- 🔄 Payment Integration
- 🔄 Order Tracking
- 🔄 Notification System
- 🔄 Multi-language Support
- 🔄 Multi-currency Support
- 🔄 AI Recommendations
- 🔄 Loyalty Program

---

## 🗺️ **Roadmap chi tiết**

### 1. 👤 **Customer Profile Management**
**Priority: HIGH** | **Estimated: 2-3 days**

#### APIs cần phát triển:
- `PUT /api/customer/profile` - Cập nhật thông tin cá nhân
- `PUT /api/customer/password` - Đổi mật khẩu
- `POST /api/customer/avatar` - Upload avatar
- `GET /api/customer/addresses` - Lấy danh sách địa chỉ
- `POST /api/customer/addresses` - Thêm địa chỉ mới
- `PUT /api/customer/addresses/{id}` - Cập nhật địa chỉ
- `DELETE /api/customer/addresses/{id}` - Xóa địa chỉ

#### DTOs cần tạo:
- `CustomerProfileDto`
- `AddressDto`
- `ChangePasswordDto`

#### Entities cần cập nhật:
- `User` entity (thêm các trường address, avatar, etc.)

---

### 2. 🔍 **Advanced Search & Filter**
**Priority: HIGH** | **Estimated: 3-4 days**

#### APIs cần phát triển:
- `GET /api/products/search` - Tìm kiếm sản phẩm
- `GET /api/products/filter` - Lọc sản phẩm theo nhiều tiêu chí
- `GET /api/products/sort` - Sắp xếp sản phẩm

#### Query Parameters:
```
?q=keyword&category=1&minPrice=1000000&maxPrice=10000000&material=gold&sort=price_asc&page=0&size=20
```

#### DTOs cần tạo:
- `ProductSearchDto`
- `ProductFilterDto`
- `SearchResultDto`

#### Features:
- Full-text search
- Filter by category, price range, material, brand
- Sort by price, rating, popularity, date
- Pagination

---

### 3. ❤️ **Wishlist System**
**Priority: MEDIUM** | **Estimated: 2-3 days**

#### APIs cần phát triển:
- `GET /api/customer/wishlist` - Lấy danh sách yêu thích
- `POST /api/customer/wishlist` - Thêm vào wishlist
- `DELETE /api/customer/wishlist/{productId}` - Xóa khỏi wishlist
- `GET /api/customer/wishlist/notifications` - Thông báo giảm giá

#### DTOs cần tạo:
- `WishlistDto`
- `WishlistItemDto`

#### Features:
- Add/remove products to wishlist
- Price drop notifications
- Wishlist sharing

---

### 4. 💳 **Payment Integration**
**Priority: HIGH** | **Estimated: 5-7 days**

#### APIs cần phát triển:
- `GET /api/payments/methods` - Lấy danh sách phương thức thanh toán
- `POST /api/payments/process` - Xử lý thanh toán
- `GET /api/payments/{id}/status` - Kiểm tra trạng thái thanh toán
- `POST /api/payments/refund` - Hoàn tiền

#### Payment Methods:
- COD (Cash on Delivery)
- Credit Card (Visa, MasterCard)
- PayPal
- VNPay
- Momo
- Bank Transfer

#### DTOs cần tạo:
- `PaymentMethodDto`
- `PaymentRequestDto`
- `PaymentResponseDto`

#### Third-party Integrations:
- VNPay API
- Momo API
- PayPal SDK
- Stripe (for international cards)

---

### 5. 📦 **Order Tracking**
**Priority: MEDIUM** | **Estimated: 2-3 days**

#### APIs cần phát triển:
- `GET /api/orders/{id}/tracking` - Theo dõi đơn hàng
- `GET /api/orders/{id}/shipping` - Thông tin vận chuyển
- `POST /api/orders/{id}/cancel` - Hủy đơn hàng
- `POST /api/orders/{id}/return` - Yêu cầu đổi trả

#### DTOs cần tạo:
- `OrderTrackingDto`
- `ShippingInfoDto`
- `OrderStatusDto`

#### Features:
- Real-time order status updates
- Shipping tracking integration (GHTK, GHN, Viettel Post)
- Order cancellation within time limit
- Return/refund requests

---

### 6. 🔔 **Notification System**
**Priority: MEDIUM** | **Estimated: 4-5 days**

#### APIs cần phát triển:
- `GET /api/customer/notifications` - Lấy thông báo
- `PUT /api/customer/notifications/{id}/read` - Đánh dấu đã đọc
- `PUT /api/customer/notifications/preferences` - Cài đặt thông báo
- `POST /api/customer/notifications/subscribe` - Đăng ký push notification

#### Notification Types:
- Order status updates
- Promotion notifications
- Price drop alerts
- System announcements

#### DTOs cần tạo:
- `NotificationDto`
- `NotificationPreferencesDto`

#### Integrations:
- Email service (SendGrid, AWS SES)
- SMS service (Twilio, AWS SNS)
- Push notifications (Firebase, OneSignal)

---

### 7. 🌐 **Multi-language Support**
**Priority: LOW** | **Estimated: 2-3 days**

#### APIs cần phát triển:
- `GET /api/languages` - Lấy danh sách ngôn ngữ
- `PUT /api/customer/language` - Đổi ngôn ngữ
- `GET /api/translations/{lang}` - Lấy bản dịch

#### Supported Languages:
- Vietnamese (vi)
- English (en)
- Chinese (zh)
- Japanese (ja)

#### DTOs cần tạo:
- `LanguageDto`
- `TranslationDto`

---

### 8. 💰 **Multi-currency Support**
**Priority: LOW** | **Estimated: 3-4 days**

#### APIs cần phát triển:
- `GET /api/currencies` - Lấy danh sách tiền tệ
- `PUT /api/customer/currency` - Đổi tiền tệ
- `GET /api/currencies/rates` - Tỷ giá hối đoái

#### Supported Currencies:
- VND (Vietnamese Dong)
- USD (US Dollar)
- EUR (Euro)
- CNY (Chinese Yuan)

#### DTOs cần tạo:
- `CurrencyDto`
- `ExchangeRateDto`

#### Features:
- Real-time exchange rates
- Price conversion
- Currency preference

---

### 9. 🤖 **AI Product Recommendations**
**Priority: LOW** | **Estimated: 5-7 days**

#### APIs cần phát triển:
- `GET /api/recommendations/personalized` - Gợi ý cá nhân hóa
- `GET /api/recommendations/similar/{productId}` - Sản phẩm tương tự
- `GET /api/recommendations/trending` - Sản phẩm xu hướng
- `POST /api/recommendations/feedback` - Phản hồi gợi ý

#### DTOs cần tạo:
- `RecommendationDto`
- `RecommendationFeedbackDto`

#### AI Features:
- Collaborative filtering
- Content-based filtering
- Trending products
- Seasonal recommendations

---

### 10. 🏆 **Loyalty Program**
**Priority: LOW** | **Estimated: 4-5 days**

#### APIs cần phát triển:
- `GET /api/customer/loyalty/points` - Điểm tích lũy
- `GET /api/customer/loyalty/tier` - Hạng thành viên
- `GET /api/customer/loyalty/rewards` - Phần thưởng
- `POST /api/customer/loyalty/redeem` - Đổi điểm

#### DTOs cần tạo:
- `LoyaltyPointsDto`
- `LoyaltyTierDto`
- `RewardDto`

#### Features:
- Points accumulation
- Tier-based benefits
- Reward redemption
- Special member offers

---

## 🛠️ **Technical Implementation**

### Database Schema Updates
```sql
-- Customer addresses
CREATE TABLE customer_addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type ENUM('HOME', 'WORK', 'OTHER') DEFAULT 'HOME',
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Customer preferences
CREATE TABLE customer_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    language VARCHAR(5) DEFAULT 'vi',
    currency VARCHAR(3) DEFAULT 'VND',
    email_notifications BOOLEAN DEFAULT TRUE,
    sms_notifications BOOLEAN DEFAULT FALSE,
    push_notifications BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Loyalty program
CREATE TABLE loyalty_points (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    points INT NOT NULL DEFAULT 0,
    tier ENUM('BRONZE', 'SILVER', 'GOLD', 'PLATINUM') DEFAULT 'BRONZE',
    total_spent DECIMAL(15,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### New Controllers
- `CustomerController` - Profile management
- `SearchController` - Product search & filter
- `WishlistController` - Wishlist management
- `PaymentController` - Payment processing
- `NotificationController` - Notification management
- `LanguageController` - Multi-language support
- `CurrencyController` - Multi-currency support
- `RecommendationController` - AI recommendations
- `LoyaltyController` - Loyalty program

### New Services
- `CustomerService` - Customer profile operations
- `SearchService` - Product search logic
- `WishlistService` - Wishlist operations
- `PaymentService` - Payment processing
- `NotificationService` - Notification management
- `LanguageService` - Language switching
- `CurrencyService` - Currency conversion
- `RecommendationService` - AI recommendations
- `LoyaltyService` - Loyalty program logic

---

## 📅 **Timeline**

### Phase 1 (Weeks 1-2): Core Features
- Customer Profile Management
- Advanced Search & Filter
- Payment Integration

### Phase 2 (Weeks 3-4): Enhanced Features
- Wishlist System
- Order Tracking
- Notification System

### Phase 3 (Weeks 5-6): Advanced Features
- Multi-language Support
- Multi-currency Support
- AI Recommendations

### Phase 4 (Weeks 7-8): Premium Features
- Loyalty Program
- Advanced Analytics
- Performance Optimization

---

## 🎯 **Success Metrics**

- **User Engagement**: Tăng 40% thời gian trên site
- **Conversion Rate**: Tăng 25% tỷ lệ chuyển đổi
- **Customer Satisfaction**: Đạt 4.5/5 rating
- **Revenue Growth**: Tăng 30% doanh thu
- **User Retention**: Tăng 50% tỷ lệ quay lại

---

## 🔧 **Development Guidelines**

### Code Standards
- Follow RESTful API conventions
- Use proper HTTP status codes
- Implement comprehensive error handling
- Add input validation
- Write unit tests for all endpoints

### Security
- Implement rate limiting
- Add input sanitization
- Use HTTPS for all endpoints
- Implement proper authentication
- Add CSRF protection

### Performance
- Implement caching strategies
- Use database indexing
- Optimize queries
- Implement pagination
- Add response compression

---

## 📚 **Documentation**

- API documentation with examples
- Integration guides for third-party services
- Error code reference
- Rate limiting documentation
- Security best practices

---

**Tổng kết**: Roadmap này sẽ biến JewelryStore thành một nền tảng thương mại điện tử hoàn chỉnh với trải nghiệm khách hàng tối ưu và các tính năng hiện đại.
