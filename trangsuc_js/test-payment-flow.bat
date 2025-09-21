@echo off
echo ========================================
echo Testing Payment Flow with Correct Status Updates
echo ========================================

echo.
echo 1. Creating order...
curl -X POST http://localhost:8080/api/payment/create-order ^
  -H "Content-Type: application/json" ^
  -d "{\"shippingName\": \"Test User\", \"shippingPhone\": \"0123456789\", \"shippingAddress\": \"123 Test Street\", \"items\": [{\"productId\": 1, \"quantity\": 1, \"sizeValue\": \"M\", \"colorValue\": \"Đỏ\"}]}"

echo.
echo.
echo 2. Creating payment (will set payment_reference = orderCode)...
curl -X POST http://localhost:8080/api/payment/create-payment/1

echo.
echo.
echo 3. Confirming payment (will update status and payment_status)...
curl -X POST http://localhost:8080/api/payment/confirm-payment ^
  -H "Content-Type: application/json" ^
  -d "{\"orderCode\": \"1001234\", \"paymentId\": \"test_payment_id\"}"

echo.
echo.
echo 4. Checking database status...
echo SELECT id, order_number, status, payment_status, payment_reference FROM orders WHERE id = 1;

echo.
echo.
echo ========================================
echo Expected Results:
echo ========================================
echo 1. Order created: status=PENDING, payment_status=PENDING, payment_reference=null
echo 2. Payment created: status=PENDING, payment_reference=orderCode
echo 3. Payment confirmed: status=PROCESSING, payment_status=SUCCESS, payment_reference=orderCode
echo ========================================
pause
