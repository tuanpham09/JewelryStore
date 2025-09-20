package org.example.trangsuc_js.controller.admin;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.admin.*;
import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductImageDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.dto.report.ReportDto;
import org.example.trangsuc_js.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // ========== PRODUCT MANAGEMENT ==========
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductUpsertDto dto) {
        ProductDto product = adminService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductUpsertDto dto) {
        ProductDto product = adminService.updateProduct(id, dto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = adminService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products/{id}/images")
    public ResponseEntity<ProductDto> uploadProductImage(
            @PathVariable Long id, 
            @RequestParam("file") MultipartFile file) {
        ProductDto product = adminService.uploadProductImage(id, file);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/products/{id}/images")
    public ResponseEntity<Void> deleteProductImage(
            @PathVariable Long id, 
            @RequestParam String imageUrl) {
        adminService.deleteProductImage(id, imageUrl);
        return ResponseEntity.noContent().build();
    }

    // ========== PRODUCT IMAGE MANAGEMENT ==========
    @GetMapping("/products/{id}/images")
    public ResponseEntity<List<ProductImageDto>> getProductImages(@PathVariable Long id) {
        List<ProductImageDto> images = adminService.getProductImages(id);
        return ResponseEntity.ok(images);
    }

    @PostMapping("/products/{id}/images/url")
    public ResponseEntity<ProductImageDto> addProductImage(
            @PathVariable Long id,
            @RequestParam String imageUrl,
            @RequestParam(required = false) String altText) {
        ProductImageDto image = adminService.addProductImage(id, imageUrl, altText);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
    }

    @PutMapping("/products/images/{imageId}")
    public ResponseEntity<ProductImageDto> updateProductImage(
            @PathVariable Long imageId,
            @RequestParam(required = false) String altText,
            @RequestParam(required = false) Integer sortOrder) {
        ProductImageDto image = adminService.updateProductImage(imageId, altText, sortOrder);
        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/products/images/{imageId}")
    public ResponseEntity<Void> deleteProductImageById(@PathVariable Long imageId) {
        adminService.deleteProductImageById(imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{id}/images/{imageId}/primary")
    public ResponseEntity<ProductImageDto> setPrimaryImage(
            @PathVariable Long id,
            @PathVariable Long imageId) {
        ProductImageDto image = adminService.setPrimaryImage(id, imageId);
        return ResponseEntity.ok(image);
    }

    @PutMapping("/products/{id}/images/reorder")
    public ResponseEntity<ProductImageDto> reorderProductImages(
            @PathVariable Long id,
            @RequestBody List<Long> imageIds) {
        ProductImageDto result = adminService.reorderProductImages(id, imageIds);
        return ResponseEntity.ok(result);
    }

    // ========== CATEGORY MANAGEMENT ==========
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        CategoryDto category = adminService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        CategoryDto category = adminService.updateCategory(id, dto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categories = adminService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // ========== USER MANAGEMENT ==========
    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<AdminUserDto> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable Long id) {
        AdminUserDto user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<AdminUserDto> updateUserStatus(
            @PathVariable Long id, 
            @RequestParam boolean enabled) {
        AdminUserDto user = adminService.updateUserStatus(id, enabled);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<AdminUserDto> updateUserRoles(
            @PathVariable Long id, 
            @RequestBody List<String> roles) {
        AdminUserDto user = adminService.updateUserRoles(id, roles);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ========== ORDER MANAGEMENT ==========
    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderDto>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<AdminOrderDto> orders = adminService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<AdminOrderDto> getOrderById(@PathVariable Long id) {
        AdminOrderDto order = adminService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<AdminOrderDto> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        AdminOrderDto order = adminService.updateOrderStatus(id, status);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<AdminOrderDto> cancelOrder(
            @PathVariable Long id, 
            @RequestParam String reason) {
        AdminOrderDto order = adminService.cancelOrder(id, reason);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<AdminOrderDto>> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<AdminOrderDto> orders = adminService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }

    // ========== INVENTORY MANAGEMENT ==========
    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryDto>> getInventoryStatus() {
        List<InventoryDto> inventory = adminService.getInventoryStatus();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/inventory/low-stock")
    public ResponseEntity<List<InventoryDto>> getLowStockProducts() {
        List<InventoryDto> products = adminService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/inventory/out-of-stock")
    public ResponseEntity<List<InventoryDto>> getOutOfStockProducts() {
        List<InventoryDto> products = adminService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/inventory/{productId}/stock")
    public ResponseEntity<InventoryDto> updateProductStock(
            @PathVariable Long productId, 
            @RequestParam Integer newStock) {
        InventoryDto inventory = adminService.updateProductStock(productId, newStock);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/inventory/{productId}/min-max-stock")
    public ResponseEntity<InventoryDto> setMinMaxStock(
            @PathVariable Long productId, 
            @RequestParam Integer minStock, 
            @RequestParam Integer maxStock) {
        InventoryDto inventory = adminService.setMinMaxStock(productId, minStock, maxStock);
        return ResponseEntity.ok(inventory);
    }

    // ========== PROMOTION MANAGEMENT ==========
    @PostMapping("/promotions")
    public ResponseEntity<PromotionDto> createPromotion(@RequestBody PromotionDto dto) {
        PromotionDto promotion = adminService.createPromotion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(promotion);
    }

    @PutMapping("/promotions/{id}")
    public ResponseEntity<PromotionDto> updatePromotion(@PathVariable Long id, @RequestBody PromotionDto dto) {
        PromotionDto promotion = adminService.updatePromotion(id, dto);
        return ResponseEntity.ok(promotion);
    }

    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        adminService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<PromotionDto>> getPromotions() {
        List<PromotionDto> promotions = adminService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    @PutMapping("/promotions/{id}/toggle")
    public ResponseEntity<PromotionDto> togglePromotionStatus(@PathVariable Long id) {
        PromotionDto promotion = adminService.togglePromotionStatus(id);
        return ResponseEntity.ok(promotion);
    }

    // ========== DASHBOARD & STATISTICS ==========
    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        DashboardStatsDto stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/reports/revenue")
    public ResponseEntity<ReportDto> getRevenueReport() {
        ReportDto report = adminService.getRevenueReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/revenue/date-range")
    public ResponseEntity<ReportDto> getRevenueReportByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        ReportDto report = adminService.getRevenueReportByDateRange(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    // ========== EXPORT REPORTS ==========
    @GetMapping("/reports/orders/export")
    public ResponseEntity<byte[]> exportOrdersToExcel(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        byte[] excelData = adminService.exportOrdersToExcel(startDate, endDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "orders_report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    @GetMapping("/reports/products/export")
    public ResponseEntity<byte[]> exportProductsToExcel() {
        byte[] excelData = adminService.exportProductsToExcel();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "products_report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    @GetMapping("/reports/customers/export")
    public ResponseEntity<byte[]> exportCustomersToExcel() {
        byte[] excelData = adminService.exportCustomersToExcel();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "customers_report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // ========== SHIPPING MANAGEMENT ==========
    @GetMapping("/shipping")
    public ResponseEntity<List<ShippingDto>> getAllShippings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ShippingDto> shippings = adminService.getAllShippings(pageable);
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/shipping/{id}")
    public ResponseEntity<ShippingDto> getShippingById(@PathVariable Long id) {
        ShippingDto shipping = adminService.getShippingById(id);
        return ResponseEntity.ok(shipping);
    }

    @GetMapping("/shipping/order/{orderId}")
    public ResponseEntity<ShippingDto> getShippingByOrderId(@PathVariable Long orderId) {
        ShippingDto shipping = adminService.getShippingByOrderId(orderId);
        return ResponseEntity.ok(shipping);
    }

    @PostMapping("/shipping")
    public ResponseEntity<ShippingDto> createShipping(@RequestBody ShippingDto dto) {
        ShippingDto shipping = adminService.createShipping(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipping);
    }

    @PutMapping("/shipping/{id}/status")
    public ResponseEntity<ShippingDto> updateShippingStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        ShippingDto shipping = adminService.updateShippingStatus(id, status);
        return ResponseEntity.ok(shipping);
    }

    @PutMapping("/shipping/{id}/tracking")
    public ResponseEntity<ShippingDto> updateTrackingNumber(
            @PathVariable Long id, 
            @RequestParam String trackingNumber) {
        ShippingDto shipping = adminService.updateTrackingNumber(id, trackingNumber);
        return ResponseEntity.ok(shipping);
    }

    @GetMapping("/shipping/status/{status}")
    public ResponseEntity<List<ShippingDto>> getShippingsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ShippingDto> shippings = adminService.getShippingsByStatus(status, pageable);
        return ResponseEntity.ok(shippings);
    }

    @GetMapping("/shipping/method/{method}")
    public ResponseEntity<List<ShippingDto>> getShippingsByMethod(
            @PathVariable String method,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ShippingDto> shippings = adminService.getShippingsByMethod(method, pageable);
        return ResponseEntity.ok(shippings);
    }
}
