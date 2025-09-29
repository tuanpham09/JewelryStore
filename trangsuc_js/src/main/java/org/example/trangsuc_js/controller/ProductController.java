package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.search.SearchResultDto;
import org.example.trangsuc_js.service.ProductService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Products retrieved successfully",
            products
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Product retrieved successfully",
            productDto
        ));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDto> productDtos = productService.getProductsByCategory(categoryId);
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Products retrieved successfully",
            productDtos
        ));
    }
    
    @GetMapping("/{id}/related")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getRelatedProducts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "4") int limit) {
        List<ProductDto> relatedProducts = productService.getRelatedProducts(id, limit);
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Related products retrieved successfully",
            relatedProducts
        ));
    }
    
    @PostMapping("/{id}/view")
    public ResponseEntity<ApiResponse<ProductDto>> incrementViewCount(@PathVariable Long id) {
        ProductDto productDto = productService.incrementViewCount(id);
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "View count incremented successfully",
            productDto
        ));
    }

    @GetMapping("/deal-of-the-day")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getDealOfTheDayProducts() {
        List<ProductDto> products = productService.getDealOfTheDayProducts();
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Deal of the day products retrieved successfully",
            products
        ));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getFeaturedProducts() {
        List<ProductDto> products = productService.getFeaturedProducts();
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Featured products retrieved successfully",
            products
        ));
    }

    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getNewArrivals() {
        List<ProductDto> products = productService.getNewArrivals();
        
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "New arrivals retrieved successfully",
            products
        ));
    }

}
