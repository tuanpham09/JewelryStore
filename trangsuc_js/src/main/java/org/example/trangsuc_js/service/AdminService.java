package org.example.trangsuc_js.service;


import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.dto.report.ReportDto;
import org.example.trangsuc_js.entity.Order;

import java.util.List;

public interface AdminService {
    // Product
    ProductDto createProduct(ProductUpsertDto dto);
    ProductDto updateProduct(Long id, ProductUpsertDto dto);
    void deleteProduct(Long id);
    List<ProductDto> getAllProducts();

    // Category
    CategoryDto createCategory(CategoryDto dto);
    void deleteCategory(Long id);
    List<CategoryDto> getAllCategories();

    // Orders
    List<Order> getAllOrders();
    Order updateOrderStatus(Long id, String status);

    // Reports
    ReportDto getRevenueReport();
}
