# Hướng dẫn Debug Payment Flow

## 🐛 Vấn đề hiện tại
Sau khi tạo đơn hàng thành công, hệ thống chưa tự động chuyển đến trang thanh toán PayOS.

## 🔍 Các bước debug

### 1. **Kiểm tra Backend Response**

Test endpoint tạo payment link (public):
```bash
curl -X POST http://localhost:8080/api/payment/create-payment-link \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Test Product",
    "description": "Test",
    "returnUrl": "http://localhost:4200/checkout/success",
    "cancelUrl": "http://localhost:4200/checkout/cancel",
    "price": 100000
  }'
```

**Expected Response:**
```json
{
  "error": 0,
  "message": "success",
  "data": {
    "checkoutUrl": "https://pay.payos.vn/web/...",
    "paymentLinkId": "...",
    "qrCode": "..."
  }
}
```

### 2. **Kiểm tra Frontend Flow**

#### **Bước 1: Test Payment Link (Không cần auth)**
1. Truy cập: `http://localhost:4200/test-payment`
2. Click "Test Create Payment Link"
3. Kiểm tra response trong console
4. Click "Go to PayOS" để test redirect

#### **Bước 2: Test Order Creation (Cần auth)**
1. Đăng nhập vào hệ thống
2. Truy cập: `http://localhost:4200/test-payment`
3. Click "Test Create Order"
4. Kiểm tra response và lưu `orderId`

#### **Bước 3: Test Payment Creation**
1. Nhập `orderId` từ bước 2
2. Click "Test Create Payment"
3. Kiểm tra response có `checkoutUrl` không
4. Click "Go to PayOS" để test redirect

### 3. **Debug Checkout Component**

#### **Kiểm tra Console Logs**
Mở Developer Tools (F12) và kiểm tra console khi thực hiện checkout:

```javascript
// Expected logs:
Creating order: {...}
Order created: {...}
Creating payment for order: 123
Payment response: {...}
Payment created successfully: {...}
Redirecting to PayOS: https://pay.payos.vn/web/...
```

#### **Kiểm tra Network Tab**
1. Mở Network tab trong Developer Tools
2. Thực hiện checkout
3. Kiểm tra các request:
   - `POST /api/payment/create-order` - Status 200
   - `POST /api/payment/create-payment/{orderId}` - Status 200

### 4. **Các lỗi thường gặp**

#### **Lỗi 1: "No checkout URL found in response"**
```javascript
// Check response structure
console.log('Payment response:', response);
// Should have: response.data.checkoutUrl
```

#### **Lỗi 2: "403 Forbidden"**
- Endpoint cần authentication
- Kiểm tra JWT token trong request headers
- Đảm bảo user đã đăng nhập

#### **Lỗi 3: "500 Internal Server Error"**
- Kiểm tra backend logs
- Có thể là lỗi validation PayOS
- Kiểm tra `total_amount` trong database

### 5. **Sửa lỗi trong Checkout Component**

#### **Vấn đề: Response structure không đúng**
```typescript
// Wrong:
if (response.data.data?.checkoutUrl) {
  window.location.href = response.data.data.checkoutUrl;
}

// Correct:
if (response.data?.checkoutUrl) {
  window.location.href = response.data.checkoutUrl;
}
```

#### **Vấn đề: Method name không đúng**
```typescript
// Wrong:
this.paymentService.createPayment(orderId)

// Correct:
this.paymentService.createPaymentForOrder(orderId)
```

### 6. **Test Flow hoàn chỉnh**

#### **Scenario 1: Direct Payment Link**
1. Vào `/test-payment`
2. Click "Test Create Payment Link"
3. Click "Go to PayOS"
4. ✅ Should redirect to PayOS checkout

#### **Scenario 2: Order + Payment Flow**
1. Login vào hệ thống
2. Vào `/test-payment`
3. Click "Test Create Order" → Lưu orderId
4. Nhập orderId → Click "Test Create Payment"
5. Click "Go to PayOS"
6. ✅ Should redirect to PayOS checkout

#### **Scenario 3: Real Checkout Flow**
1. Login vào hệ thống
2. Thêm sản phẩm vào cart
3. Vào `/checkout`
4. Điền thông tin và click "Thanh toán"
5. ✅ Should redirect to PayOS checkout

### 7. **Backend Debug**

#### **Kiểm tra Order Data**
```sql
SELECT * FROM orders WHERE id = 1;
SELECT * FROM order_items WHERE order_id = 1;
```

#### **Kiểm tra Payment Data**
```sql
SELECT * FROM payments WHERE order_id = 1;
```

#### **Backend Logs**
```bash
# Check Spring Boot logs for:
# - Order creation
# - Payment creation
# - PayOS API calls
# - Error messages
```

### 8. **Common Fixes**

#### **Fix 1: Update PaymentService**
```typescript
// Add missing method
createOrder(orderData: any): Observable<any> {
  return this.http.post(`${this.apiUrl}/create-order`, orderData);
}
```

#### **Fix 2: Update Checkout Component**
```typescript
// Use correct method and response structure
this.paymentService.createPaymentForOrder(orderId).subscribe({
  next: (response: any) => {
    if (response.success && response.data?.checkoutUrl) {
      window.location.href = response.data.checkoutUrl;
    }
  }
});
```

#### **Fix 3: Add Debug Logs**
```typescript
console.log('Payment response:', response);
console.log('Checkout URL:', response.data?.checkoutUrl);
```

## 🎯 Kết quả mong đợi

Sau khi sửa tất cả lỗi, flow sẽ hoạt động như sau:

1. **User click "Thanh toán"** → Tạo đơn hàng
2. **Order created successfully** → Tạo payment
3. **Payment created successfully** → Redirect to PayOS
4. **User complete payment** → Redirect back to success page
5. **Order status updated** → Webhook processed

## 📞 Support

Nếu vẫn gặp vấn đề, hãy:
1. Kiểm tra console logs
2. Kiểm tra network requests
3. Kiểm tra backend logs
4. Test từng bước một cách riêng biệt
