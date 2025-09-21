# Test API Script
Write-Host "Testing Jewelry Store APIs..." -ForegroundColor Green

# Test 1: Public Categories API
Write-Host "`n1. Testing Public Categories API..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/categories" -Method GET -ContentType "application/json"
    Write-Host "✅ Public Categories API: SUCCESS" -ForegroundColor Green
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Cyan
    Write-Host "Response Length: $($response.Content.Length) characters" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Public Categories API: FAILED" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Admin Categories API (without auth - should fail)
Write-Host "`n2. Testing Admin Categories API (without auth)..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/admin/categories" -Method GET -ContentType "application/json"
    Write-Host "❌ Admin Categories API: Should have failed but didn't!" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 403) {
        Write-Host "✅ Admin Categories API: Correctly requires authentication (403 Forbidden)" -ForegroundColor Green
    } else {
        Write-Host "❌ Admin Categories API: Unexpected error" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 3: Login API
Write-Host "`n3. Testing Login API..." -ForegroundColor Yellow
try {
    $loginBody = @{
        email = "admin@gmail.com"
        password = "admin123"
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body $loginBody
    Write-Host "✅ Login API: SUCCESS" -ForegroundColor Green
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Cyan
    
    # Parse response to get token
    $responseData = $response.Content | ConvertFrom-Json
    if ($responseData.token) {
        Write-Host "Token received: $($responseData.token.Substring(0, 20))..." -ForegroundColor Cyan
        
        # Test 4: Admin Categories API with token
        Write-Host "`n4. Testing Admin Categories API (with auth)..." -ForegroundColor Yellow
        try {
            $headers = @{
                "Authorization" = "Bearer $($responseData.token)"
                "Content-Type" = "application/json"
            }
            $adminResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/admin/categories" -Method GET -Headers $headers
            Write-Host "✅ Admin Categories API with auth: SUCCESS" -ForegroundColor Green
            Write-Host "Status: $($adminResponse.StatusCode)" -ForegroundColor Cyan
            Write-Host "Response Length: $($adminResponse.Content.Length) characters" -ForegroundColor Cyan
        } catch {
            Write-Host "❌ Admin Categories API with auth: FAILED" -ForegroundColor Red
            Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        }
    } else {
        Write-Host "❌ No token in response" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Login API: FAILED" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nAPI Testing Complete!" -ForegroundColor Green
