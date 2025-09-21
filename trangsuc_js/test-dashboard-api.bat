@echo off
echo ========================================
echo Testing Dashboard API with Chart Data
echo ========================================

echo.
echo 1. Testing dashboard stats API...
curl -X GET http://localhost:8080/api/admin/dashboard/stats

echo.
echo.
echo ========================================
echo Expected Response Structure:
echo ========================================
echo {
echo   "totalRevenue": 1500000.0,
echo   "todayRevenue": 500000.0,
echo   "monthlyRevenue": 1200000.0,
echo   "totalOrders": 25,
echo   "todayOrders": 3,
echo   "monthlyOrders": 15,
echo   "totalCustomers": 10,
echo   "newCustomersToday": 2,
echo   "totalProducts": 50,
echo   "lowStockProducts": 5,
echo   "outOfStockProducts": 2,
echo   "pendingOrders": 8,
echo   "activePromotions": 3,
echo   "revenueChart": [
echo     {
echo       "date": "2024-09-15",
echo       "revenue": 200000.0,
echo       "orders": 2
echo     }
echo   ],
echo   "orderStatusStats": [
echo     {
echo       "status": "PENDING",
echo       "count": 5,
echo       "totalValue": 800000.0
echo     },
echo     {
echo       "status": "PROCESSING", 
echo       "count": 3,
echo       "totalValue": 600000.0
echo     }
echo   ]
echo }
echo ========================================
pause
