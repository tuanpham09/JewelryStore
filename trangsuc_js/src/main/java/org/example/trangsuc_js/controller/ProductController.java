package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.search.SearchResultDto;
import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.mapper.ProductMapper;
import org.example.trangsuc_js.repository.ProductRepository;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<ApiResponse<SearchResultDto<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        
        // Create sort
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        
        // Create pageable
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Get products with pagination
        Page<Product> productPage = productRepository.findAll(pageable);
        
        // Convert to DTOs
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
        
        // Create search result DTO
        SearchResultDto<ProductDto> searchResult = new SearchResultDto<>(
            productDtos,
            productPage.getTotalElements(),
            productPage.getTotalPages(),
            productPage.getNumber(),
            productPage.getSize(),
            productPage.hasNext(),
            productPage.hasPrevious(),
            productPage.getTotalElements(),
            "0ms"
        );
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Products retrieved successfully",
            searchResult
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
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setStock(product.getStock());
        dto.setThumbnail(product.getThumbnail());
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setIsNew(product.getIsNew());
        dto.setIsBestseller(product.getIsBestseller());
        dto.setIsOnSale(product.isOnSale());
        dto.setBrand(product.getBrand());
        dto.setMaterial(product.getMaterial());
        dto.setColor(product.getColor());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
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
