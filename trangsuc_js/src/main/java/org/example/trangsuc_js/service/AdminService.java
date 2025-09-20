package org.example.trangsuc_js.service;


import org.example.trangsuc_js.dto.admin.*;
import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductImageDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.dto.report.ReportDto;
import org.example.trangsuc_js.dto.review.ReviewDto;
import org.example.trangsuc_js.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {
    // Product Management
    ProductDto createProduct(ProductUpsertDto dto);
    ProductDto updateProduct(Long id, ProductUpsertDto dto);
    void deleteProduct(Long id);
    List<ProductDto> getAllProducts();
    ProductDto uploadProductImage(Long productId, MultipartFile file);
    void deleteProductImage(Long productId, String imageUrl);
    
    // ProductImage Management
    List<ProductImageDto> getProductImages(Long productId);
    ProductImageDto addProductImage(Long productId, String imageUrl, String altText);
    ProductImageDto updateProductImage(Long imageId, String altText, Integer sortOrder);
    void deleteProductImageById(Long imageId);
    ProductImageDto setPrimaryImage(Long productId, Long imageId);
    ProductImageDto reorderProductImages(Long productId, List<Long> imageIds);

    // Category Management
    CategoryDto createCategory(CategoryDto dto);
    CategoryDto updateCategory(Long id, CategoryDto dto);
    void deleteCategory(Long id);
    List<CategoryDto> getAllCategories();

    // User Management
    List<AdminUserDto> getAllUsers(Pageable pageable);
    AdminUserDto getUserById(Long id);
    AdminUserDto updateUserStatus(Long id, boolean enabled);
    AdminUserDto updateUserRoles(Long id, List<String> roles);
    void deleteUser(Long id);

    // Order Management
    List<AdminOrderDto> getAllOrders(Pageable pageable);
    AdminOrderDto getOrderById(Long id);
    AdminOrderDto updateOrderStatus(Long id, String status);
    AdminOrderDto cancelOrder(Long id, String reason);
    List<AdminOrderDto> getOrdersByStatus(String status, Pageable pageable);

    // Inventory Management
    List<InventoryDto> getInventoryStatus();
    List<InventoryDto> getLowStockProducts();
    List<InventoryDto> getOutOfStockProducts();
    InventoryDto updateProductStock(Long productId, Integer newStock);
    InventoryDto setMinMaxStock(Long productId, Integer minStock, Integer maxStock);

    // Promotion Management
    PromotionDto createPromotion(PromotionDto dto);
    PromotionDto updatePromotion(Long id, PromotionDto dto);
    void deletePromotion(Long id);
    List<PromotionDto> getAllPromotions();
    PromotionDto togglePromotionStatus(Long id);

    // Dashboard & Statistics
    DashboardStatsDto getDashboardStats();
    ReportDto getRevenueReport();
    ReportDto getRevenueReportByDateRange(String startDate, String endDate);
    List<ProductDto> getTopSellingProducts(int limit);
    List<AdminUserDto> getTopCustomers(int limit);
    byte[] exportOrdersToExcel(String startDate, String endDate);
    byte[] exportProductsToExcel();
    byte[] exportCustomersToExcel();

    // Shipping Management
    List<ShippingDto> getAllShippings(Pageable pageable);
    ShippingDto getShippingById(Long id);
    ShippingDto getShippingByOrderId(Long orderId);
    ShippingDto createShipping(ShippingDto dto);
    ShippingDto updateShippingStatus(Long id, String status);
    ShippingDto updateTrackingNumber(Long id, String trackingNumber);
    List<ShippingDto> getShippingsByStatus(String status, Pageable pageable);
    List<ShippingDto> getShippingsByMethod(String method, Pageable pageable);
    
    // Review Management
    List<ReviewDto> getAllReviews(Pageable pageable);
    ReviewDto approveReview(Long reviewId);
    ReviewDto rejectReview(Long reviewId, String reason);
    void deleteReview(Long reviewId);
}
