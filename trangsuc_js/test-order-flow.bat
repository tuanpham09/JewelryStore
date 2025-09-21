@echo off
echo ========================================
echo Testing Order Status Flow
echo ========================================

echo.
echo 1. Creating order...
curl -X POST http://localhost:8080/api/payment/create-order ^
  -H "Content-Type: application/json" ^
  -d "{\"shippingName\": \"Test User\", \"shippingPhone\": \"0123456789\", \"shippingAddress\": \"123 Test Street\", \"items\": [{\"productId\": 1, \"quantity\": 1, \"sizeValue\": \"M\", \"colorValue\": \"Đỏ\"}]}"

echo.
echo.
echo 2. Creating payment...
curl -X POST http://localhost:8080/api/payment/create-payment/1

echo.
echo.
echo 3. Confirming payment...
curl -X POST http://localhost:8080/api/payment/confirm-payment ^
  -H "Content-Type: application/json" ^
  -d "{\"orderCode\": \"1001234\", \"paymentId\": \"test_payment_id\"}"

echo.
echo.
echo 4. Checking admin dashboard stats...
curl -X GET http://localhost:8080/api/admin/dashboard/stats

echo.
echo.
echo ========================================
echo Test completed!
echo ========================================
pause
