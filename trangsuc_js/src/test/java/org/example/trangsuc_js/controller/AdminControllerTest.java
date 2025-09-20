package org.example.trangsuc_js.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trangsuc_js.dto.admin.AdminUserDto;
import org.example.trangsuc_js.dto.admin.AdminOrderDto;
import org.example.trangsuc_js.dto.admin.InventoryDto;
import org.example.trangsuc_js.dto.admin.PromotionDto;
import org.example.trangsuc_js.dto.admin.DashboardStatsDto;
import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductImageDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.dto.report.ReportDto;
import org.example.trangsuc_js.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(org.example.trangsuc_js.controller.admin.AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== PRODUCT MANAGEMENT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateProduct() throws Exception {
        ProductUpsertDto productDto = new ProductUpsertDto();
        productDto.setName("Test Product");
        productDto.setSlug("test-product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("100.00"));
        productDto.setStock(10);
        productDto.setCategoryId(1L);

        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setName("Test Product");
        expectedProduct.setPrice(new BigDecimal("100.00"));

        when(adminService.createProduct(any(ProductUpsertDto.class))).thenReturn(expectedProduct);

        mockMvc.perform(post("/api/admin/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllProducts() throws Exception {
        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setName("Product 1");

        ProductDto product2 = new ProductDto();
        product2.setId(2L);
        product2.setName("Product 2");

        List<ProductDto> products = Arrays.asList(product1, product2);

        when(adminService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/admin/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateProduct() throws Exception {
        ProductUpsertDto productDto = new ProductUpsertDto();
        productDto.setName("Updated Product");
        productDto.setPrice(new BigDecimal("150.00"));

        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setName("Updated Product");
        expectedProduct.setPrice(new BigDecimal("150.00"));

        when(adminService.updateProduct(eq(1L), any(ProductUpsertDto.class))).thenReturn(expectedProduct);

        mockMvc.perform(put("/api/admin/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(150.00));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/admin/products/1"))
                .andExpect(status().isNoContent());
    }

    // ========== CATEGORY MANAGEMENT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Test Category");
        categoryDto.setSlug("test-category");

        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(1L);
        expectedCategory.setName("Test Category");

        when(adminService.createCategory(any(CategoryDto.class))).thenReturn(expectedCategory);

        mockMvc.perform(post("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllCategories() throws Exception {
        CategoryDto category1 = new CategoryDto();
        category1.setId(1L);
        category1.setName("Category 1");

        CategoryDto category2 = new CategoryDto();
        category2.setId(2L);
        category2.setName("Category 2");

        List<CategoryDto> categories = Arrays.asList(category1, category2);

        when(adminService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    // ========== USER MANAGEMENT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        AdminUserDto user1 = new AdminUserDto();
        user1.setId(1L);
        user1.setFullName("User 1");
        user1.setEmail("user1@test.com");

        AdminUserDto user2 = new AdminUserDto();
        user2.setId(2L);
        user2.setFullName("User 2");
        user2.setEmail("user2@test.com");

        List<AdminUserDto> users = Arrays.asList(user1, user2);

        when(adminService.getAllUsers(any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/api/admin/users")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fullName").value("User 1"))
                .andExpect(jsonPath("$[1].fullName").value("User 2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUserStatus() throws Exception {
        AdminUserDto user = new AdminUserDto();
        user.setId(1L);
        user.setFullName("Test User");
        user.setEnabled(false);

        when(adminService.updateUserStatus(eq(1L), eq(false))).thenReturn(user);

        mockMvc.perform(put("/api/admin/users/1/status")
                .param("enabled", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false));
    }

    // ========== ORDER MANAGEMENT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllOrders() throws Exception {
        AdminOrderDto order1 = new AdminOrderDto();
        order1.setId(1L);
        order1.setOrderNumber("ORD-001");
        order1.setStatus("PENDING");

        AdminOrderDto order2 = new AdminOrderDto();
        order2.setId(2L);
        order2.setOrderNumber("ORD-002");
        order2.setStatus("DELIVERED");

        List<AdminOrderDto> orders = Arrays.asList(order1, order2);

        when(adminService.getAllOrders(any(Pageable.class))).thenReturn(orders);

        mockMvc.perform(get("/api/admin/orders")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-001"))
                .andExpect(jsonPath("$[1].orderNumber").value("ORD-002"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateOrderStatus() throws Exception {
        AdminOrderDto order = new AdminOrderDto();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        order.setStatus("SHIPPED");

        when(adminService.updateOrderStatus(eq(1L), eq("SHIPPED"))).thenReturn(order);

        mockMvc.perform(put("/api/admin/orders/1/status")
                .param("status", "SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    // ========== INVENTORY MANAGEMENT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetInventoryStatus() throws Exception {
        InventoryDto inventory1 = new InventoryDto();
        inventory1.setProductId(1L);
        inventory1.setProductName("Product 1");
        inventory1.setCurrentStock(10);
        inventory1.setStatus("IN_STOCK");

        InventoryDto inventory2 = new InventoryDto();
        inventory2.setProductId(2L);
        inventory2.setProductName("Product 2");
        inventory2.setCurrentStock(2);
        inventory2.setStatus("LOW_STOCK");

        List<InventoryDto> inventory = Arrays.asList(inventory1, inventory2);

        when(adminService.getInventoryStatus()).thenReturn(inventory);

        mockMvc.perform(get("/api/admin/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("IN_STOCK"))
                .andExpect(jsonPath("$[1].status").value("LOW_STOCK"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetLowStockProducts() throws Exception {
        InventoryDto product = new InventoryDto();
        product.setProductId(1L);
        product.setProductName("Low Stock Product");
        product.setCurrentStock(2);
        product.setStatus("LOW_STOCK");

        List<InventoryDto> products = Arrays.asList(product);

        when(adminService.getLowStockProducts()).thenReturn(products);

        mockMvc.perform(get("/api/admin/inventory/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("LOW_STOCK"));
    }

    // ========== PROMOTION MANAGEMENT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreatePromotion() throws Exception {
        PromotionDto promotionDto = new PromotionDto();
        promotionDto.setName("Test Promotion");
        promotionDto.setType("PERCENTAGE");
        promotionDto.setValue(new BigDecimal("10.00"));
        promotionDto.setCode("TEST10");

        PromotionDto expectedPromotion = new PromotionDto();
        expectedPromotion.setId(1L);
        expectedPromotion.setName("Test Promotion");
        expectedPromotion.setCode("TEST10");

        when(adminService.createPromotion(any(PromotionDto.class))).thenReturn(expectedPromotion);

        mockMvc.perform(post("/api/admin/promotions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promotionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Promotion"))
                .andExpect(jsonPath("$.code").value("TEST10"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllPromotions() throws Exception {
        PromotionDto promotion1 = new PromotionDto();
        promotion1.setId(1L);
        promotion1.setName("Promotion 1");
        promotion1.setActive(true);

        PromotionDto promotion2 = new PromotionDto();
        promotion2.setId(2L);
        promotion2.setName("Promotion 2");
        promotion2.setActive(false);

        List<PromotionDto> promotions = Arrays.asList(promotion1, promotion2);

        when(adminService.getAllPromotions()).thenReturn(promotions);

        mockMvc.perform(get("/api/admin/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Promotion 1"))
                .andExpect(jsonPath("$[1].name").value("Promotion 2"));
    }

    // ========== DASHBOARD & REPORTS TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDashboardStats() throws Exception {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalRevenue(new BigDecimal("10000.00"));
        stats.setTotalOrders(100L);
        stats.setTotalCustomers(50L);

        when(adminService.getDashboardStats()).thenReturn(stats);

        mockMvc.perform(get("/api/admin/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(10000.00))
                .andExpect(jsonPath("$.totalOrders").value(100))
                .andExpect(jsonPath("$.totalCustomers").value(50));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetRevenueReport() throws Exception {
        ReportDto report = new ReportDto();
        report.setTotalRevenue(new BigDecimal("5000.00"));
        report.setTotalOrders(50L);
        report.setTotalProductsSold(100L);

        when(adminService.getRevenueReport()).thenReturn(report);

        mockMvc.perform(get("/api/admin/reports/revenue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(5000.00))
                .andExpect(jsonPath("$.totalOrders").value(50))
                .andExpect(jsonPath("$.totalProductsSold").value(100));
    }

    // ========== EXPORT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testExportOrdersToExcel() throws Exception {
        byte[] excelData = "Mock Excel Data".getBytes();

        when(adminService.exportOrdersToExcel(anyString(), anyString())).thenReturn(excelData);

        mockMvc.perform(get("/api/admin/reports/orders/export")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=orders_report.xlsx"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testExportProductsToExcel() throws Exception {
        byte[] excelData = "Mock Excel Data".getBytes();

        when(adminService.exportProductsToExcel()).thenReturn(excelData);

        mockMvc.perform(get("/api/admin/reports/products/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=products_report.xlsx"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testExportCustomersToExcel() throws Exception {
        byte[] excelData = "Mock Excel Data".getBytes();

        when(adminService.exportCustomersToExcel()).thenReturn(excelData);

        mockMvc.perform(get("/api/admin/reports/customers/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=customers_report.xlsx"));
    }

    // ========== PRODUCT IMAGE MANAGEMENT TESTS ==========

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetProductImages() throws Exception {
        Long productId = 1L;
        ProductImageDto image1 = new ProductImageDto(1L, "https://example.com/image1.jpg", "Image 1", true, 0, LocalDateTime.now(), LocalDateTime.now());
        ProductImageDto image2 = new ProductImageDto(2L, "https://example.com/image2.jpg", "Image 2", false, 1, LocalDateTime.now(), LocalDateTime.now());
        List<ProductImageDto> images = Arrays.asList(image1, image2);

        when(adminService.getProductImages(productId)).thenReturn(images);

        mockMvc.perform(get("/api/admin/products/{id}/images", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].imageUrl").value("https://example.com/image1.jpg"))
                .andExpect(jsonPath("$[0].isPrimary").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].imageUrl").value("https://example.com/image2.jpg"))
                .andExpect(jsonPath("$[1].isPrimary").value(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddProductImage() throws Exception {
        Long productId = 1L;
        String imageUrl = "https://example.com/new-image.jpg";
        String altText = "New Product Image";
        ProductImageDto newImage = new ProductImageDto(3L, imageUrl, altText, false, 2, LocalDateTime.now(), LocalDateTime.now());

        when(adminService.addProductImage(productId, imageUrl, altText)).thenReturn(newImage);

        mockMvc.perform(post("/api/admin/products/{id}/images", productId)
                        .param("imageUrl", imageUrl)
                        .param("altText", altText))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.imageUrl").value(imageUrl))
                .andExpect(jsonPath("$.altText").value(altText))
                .andExpect(jsonPath("$.isPrimary").value(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateProductImage() throws Exception {
        Long imageId = 1L;
        String newAltText = "Updated Alt Text";
        Integer newSortOrder = 5;
        ProductImageDto updatedImage = new ProductImageDto(imageId, "https://example.com/image1.jpg", newAltText, true, newSortOrder, LocalDateTime.now(), LocalDateTime.now());

        when(adminService.updateProductImage(imageId, newAltText, newSortOrder)).thenReturn(updatedImage);

        mockMvc.perform(put("/api/admin/products/images/{imageId}", imageId)
                        .param("altText", newAltText)
                        .param("sortOrder", newSortOrder.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(imageId))
                .andExpect(jsonPath("$.altText").value(newAltText))
                .andExpect(jsonPath("$.sortOrder").value(newSortOrder));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteProductImageById() throws Exception {
        Long imageId = 1L;

        mockMvc.perform(delete("/api/admin/products/images/{imageId}", imageId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSetPrimaryImage() throws Exception {
        Long productId = 1L;
        Long imageId = 2L;
        ProductImageDto primaryImage = new ProductImageDto(imageId, "https://example.com/image2.jpg", "Image 2", true, 1, LocalDateTime.now(), LocalDateTime.now());

        when(adminService.setPrimaryImage(productId, imageId)).thenReturn(primaryImage);

        mockMvc.perform(put("/api/admin/products/{id}/images/{imageId}/primary", productId, imageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(imageId))
                .andExpect(jsonPath("$.isPrimary").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testReorderProductImages() throws Exception {
        Long productId = 1L;
        List<Long> imageIds = Arrays.asList(3L, 1L, 2L);
        ProductImageDto result = new ProductImageDto();

        when(adminService.reorderProductImages(productId, imageIds)).thenReturn(result);

        mockMvc.perform(put("/api/admin/products/{id}/images/reorder", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imageIds)))
                .andExpect(status().isOk());
    }
}
