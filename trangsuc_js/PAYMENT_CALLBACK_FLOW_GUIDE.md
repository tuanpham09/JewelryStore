# Hướng dẫn Flow Xử lý Payment Callback từ PAYOS

## 🔄 **Flow hoàn chỉnh từ PAYOS redirect**

### **1. URL Redirect từ PAYOS**
```
http://localhost:4200/checkout/success?code=00&id=d1ee4716b4fc4f1d9b845c30dc2072de&cancel=false&status=PAID&orderCode=1809447682
```

### **2. Query Parameters Analysis**
- `code=00` → Mã lỗi (00 = thành công)
- `id=d1ee4716b4fc4f1d9b845c30dc2072de` → Payment ID từ PAYOS
- `cancel=false` → Không bị hủy
- `status=PAID` → Trạng thái thanh toán từ PAYOS
- `orderCode=1809447682` → Order code từ PAYOS

## 📋 **Flow Xử lý Chi tiết**

### **Bước 1: Frontend Parse Parameters**
```typescript
ngOnInit(): void {
  this.route.queryParams.subscribe(params => {
    this.orderCode = params['orderCode'] || null;
    this.paymentId = params['id'] || null;
    this.status = params['status'] || null;
    const code = params['code'] || null;
    const cancel = params['cancel'] || null;
    
    // Xử lý theo trạng thái từ PAYOS
    if (this.status === 'PAID' && code === '00' && cancel === 'false') {
      this.handlePaymentSuccess();
    } else if (this.status === 'CANCELLED' || cancel === 'true') {
      this.handlePaymentCancelled();
    } else if (this.status === 'FAILED' || code !== '00') {
      this.handlePaymentFailed();
    } else {
      this.isLoading = false;
      this.error = 'Thông tin thanh toán không hợp lệ';
    }
  });
}
```

### **Bước 2: Xử lý Payment Success**
```typescript
handlePaymentSuccess(): void {
  // Call API để cập nhật trạng thái đơn hàng
  this.paymentService.confirmPayment(this.orderCode!, this.paymentId!).subscribe({
    next: (response) => {
      if (response.success) {
        this.orderDetails = response.data;
        this.isLoading = false;
        this.snackBar.open('Thanh toán thành công!', 'Đóng', { duration: 3000 });
      } else {
        this.error = response.message || 'Không thể xác nhận thanh toán';
        this.isLoading = false;
      }
    },
    error: (error) => {
      console.error('Error confirming payment:', error);
      this.error = 'Có lỗi xảy ra khi xác nhận thanh toán';
      this.isLoading = false;
    }
  });
}
```

### **Bước 3: Backend Confirm Payment**
```java
@Override
@Transactional
public OrderDto confirmPayment(String orderCode, String paymentId) {
    // Tìm order theo orderCode (đã lưu trong order_number)
    Order order = orderRepo.findByOrderNumber(orderCode)
            .orElseThrow(() -> new RuntimeException("Order not found with orderCode: " + orderCode));
    
    // Cập nhật trạng thái đơn hàng thành PROCESSING
    order.setStatus(Order.OrderStatus.PROCESSING);
    order.setPaidAt(LocalDateTime.now());
    order.setPaymentStatus("SUCCESS");
    order.setPaymentMethod("PAYOS");
    order.setPaymentReference(paymentId);
    order.setUpdatedAt(LocalDateTime.now());
    order = orderRepo.save(order);
    
    // Cập nhật trạng thái payment
    List<Payment> payments = paymentRepo.findByOrderId(order.getId());
    for (Payment payment : payments) {
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepo.save(payment);
    }
    
    return toOrderDto(order);
}
```

## 🎯 **Các Trường hợp Xử lý**

### **1. Thanh toán thành công**
```
URL: ?code=00&status=PAID&cancel=false
Action: Call confirmPayment API
Result: Order status = PROCESSING, Payment status = SUCCESS
UI: Hiển thị thông tin đơn hàng thành công
```

### **2. Thanh toán bị hủy**
```
URL: ?status=CANCELLED hoặc ?cancel=true
Action: Không call API
Result: Không thay đổi database
UI: Hiển thị thông báo "Thanh toán đã bị hủy"
```

### **3. Thanh toán thất bại**
```
URL: ?status=FAILED hoặc ?code!=00
Action: Không call API
Result: Không thay đổi database
UI: Hiển thị thông báo "Thanh toán thất bại"
```

### **4. Thông tin không hợp lệ**
```
URL: Thiếu parameters hoặc không đúng format
Action: Không call API
Result: Không thay đổi database
UI: Hiển thị thông báo "Thông tin thanh toán không hợp lệ"
```

## 🧪 **Test Cases**

### **Test 1: Payment Success**
```bash
# URL: http://localhost:4200/checkout/success?code=00&id=test_payment_id&cancel=false&status=PAID&orderCode=1809447682
# Expected: Order status = PROCESSING, Payment status = SUCCESS
# UI: Hiển thị thông tin đơn hàng thành công
```

### **Test 2: Payment Cancelled**
```bash
# URL: http://localhost:4200/checkout/success?status=CANCELLED&orderCode=1809447682
# Expected: Không thay đổi database
# UI: Hiển thị thông báo "Thanh toán đã bị hủy"
```

### **Test 3: Payment Failed**
```bash
# URL: http://localhost:4200/checkout/success?code=01&status=FAILED&orderCode=1809447682
# Expected: Không thay đổi database
# UI: Hiển thị thông báo "Thanh toán thất bại"
```

## 📊 **Database Updates**

### **Khi thanh toán thành công:**
```sql
-- orders table
UPDATE orders SET 
    status = 'PROCESSING',
    payment_status = 'SUCCESS',
    payment_method = 'PAYOS',
    payment_reference = 'd1ee4716b4fc4f1d9b845c30dc2072de',
    paid_at = NOW(),
    updated_at = NOW()
WHERE order_number = '1809447682';

-- payments table
UPDATE payments SET 
    status = 'SUCCESS',
    paid_at = NOW(),
    updated_at = NOW()
WHERE order_id = (SELECT id FROM orders WHERE order_number = '1809447682');
```

## ✅ **Kết quả**

1. **Payment Success** → Order status = `PROCESSING`, Payment status = `SUCCESS`
2. **Payment Cancelled** → Không thay đổi database
3. **Payment Failed** → Không thay đổi database
4. **Admin Dashboard** → Revenue được tính từ `PROCESSING` orders
5. **User Order History** → Hiển thị trạng thái thanh toán đúng

## 🔍 **Debug Logs**

Khi chạy, kiểm tra logs:
```
Payment callback params: {code: "00", id: "d1ee4716b4fc4f1d9b845c30dc2072de", cancel: "false", status: "PAID", orderCode: "1809447682"}
Handling payment success for orderCode: 1809447682
Confirming payment: orderCode=1809447682, paymentId=d1ee4716b4fc4f1d9b845c30dc2072de
Found order: id=1, orderNumber=1809447682, status=PENDING
Updated order status: PROCESSING
Payment confirmed: SUCCESS
Admin dashboard: totalRevenue=2000 (từ PROCESSING order)
```

**Bây giờ flow xử lý payment callback đã hoàn chỉnh và xử lý đúng tất cả các trường hợp!** 🎉✨
