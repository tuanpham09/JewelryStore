package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.mapper.ProductMapper;
import org.example.trangsuc_js.repository.ProductRepository;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        List<Product> products = productRepository.findAllWithReviews();
        List<ProductDto> productDtos = products.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Products retrieved successfully",
            productDtos
        ));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        Product product = productRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        ProductDto productDto = toProductDto(product);
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Product retrieved successfully",
            productDto
        ));
    }

    @GetMapping("/category/{categoryId}")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productRepository.findAllWithReviews().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
        
        List<ProductDto> productDtos = products.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Products retrieved successfully",
            productDtos
        ));
    }

    private ProductDto toProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setThumbnail(product.getThumbnail());
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        
        // Tính average rating và review count
        if (product.getReviews() != null && !product.getReviews().isEmpty()) {
            double avgRating = product.getReviews().stream()
                    .mapToInt(r -> r.getRating())
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
            dto.setReviewCount(product.getReviews().size());
        }
        
        return dto;
    }
}
