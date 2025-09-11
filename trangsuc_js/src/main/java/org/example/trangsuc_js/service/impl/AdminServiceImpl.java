package org.example.trangsuc_js.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.dto.report.ReportDto;
import org.example.trangsuc_js.entity.Category;
import org.example.trangsuc_js.entity.Order;
import org.example.trangsuc_js.entity.OrderItem;
import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.repository.CategoryRepository;
import org.example.trangsuc_js.repository.OrderRepository;
import org.example.trangsuc_js.repository.ProductRepository;
import org.example.trangsuc_js.service.AdminService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final OrderRepository orderRepo;

    // ---------------- Product -----------------
    @Override
    public ProductDto createProduct(ProductUpsertDto dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product p = new Product();
        p.setName(dto.getName());
        p.setSlug(dto.getSlug());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setThumbnail(dto.getThumbnail());
        p.setCategory(category);
        productRepo.save(p);
        return toDto(p);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductUpsertDto dto) {
        Product p = productRepo.findById(id).orElseThrow();
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        p.setName(dto.getName());
        p.setSlug(dto.getSlug());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setThumbnail(dto.getThumbnail());
        p.setCategory(category);
        productRepo.save(p);
        return toDto(p);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private ProductDto toDto(Product p) {
        return new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getStock(),
                p.getCategory() != null ? p.getCategory().getName() : null);
    }

    // ---------------- Category -----------------
    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setSlug(dto.getSlug());
        categoryRepo.save(c);
        dto.setId(c.getId());
        return dto;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll().stream().map(c -> {
            CategoryDto dto = new CategoryDto();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setSlug(c.getSlug());
            return dto;
        }).collect(Collectors.toList());
    }

    // ---------------- Orders -----------------
    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Order updateOrderStatus(Long id, String status) {
        Order o = orderRepo.findById(id).orElseThrow();
        o.setStatus(status);
        return orderRepo.save(o);
    }

    // ---------------- Reports -----------------
    @Override
    public ReportDto getRevenueReport() {
        List<Order> orders = orderRepo.findAll();

        BigDecimal totalRevenue = orders.stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrders = orders.size();

        long totalProductsSold = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToLong(OrderItem::getQuantity)
                .sum();

        return new ReportDto(totalRevenue, totalOrders, totalProductsSold);
    }
}
