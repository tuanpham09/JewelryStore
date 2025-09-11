package org.example.trangsuc_js.controller.admin;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.dto.report.ReportDto;
import org.example.trangsuc_js.entity.Order;
import org.example.trangsuc_js.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // --------- Product ---------
    @PostMapping("/products")
    public ProductDto createProduct(@RequestBody ProductUpsertDto dto) {
        return adminService.createProduct(dto);
    }

    @PutMapping("/products/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductUpsertDto dto) {
        return adminService.updateProduct(id, dto);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
    }

    @GetMapping("/products")
    public List<ProductDto> getProducts() {
        return adminService.getAllProducts();
    }

    // --------- Category ---------
    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody CategoryDto dto) {
        return adminService.createCategory(dto);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return adminService.getAllCategories();
    }

    // --------- Orders ---------
    @GetMapping("/orders")
    public List<Order> getOrders() {
        return adminService.getAllOrders();
    }

    @PutMapping("/orders/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return adminService.updateOrderStatus(id, status);
    }

    // --------- Reports ---------
    @GetMapping("/reports/revenue")
    public ReportDto getReport() {
        return adminService.getRevenueReport();
    }
}
