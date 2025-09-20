package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.search.ProductSearchDto;
import org.example.trangsuc_js.dto.search.SearchResultDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.service.SearchService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final SearchService searchService;
    
    @PostMapping("/products")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<SearchResultDto<ProductDto>>> searchProducts(
            @RequestBody ProductSearchDto searchDto) {
        SearchResultDto<ProductDto> result = searchService.searchProducts(searchDto);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Products searched successfully",
            result
        ));
    }
    
    @GetMapping("/products/featured")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<SearchResultDto<ProductDto>>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        SearchResultDto<ProductDto> result = searchService.getFeaturedProducts(page, size);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Featured products retrieved successfully",
            result
        ));
    }
    
    @GetMapping("/products/new")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<SearchResultDto<ProductDto>>> getNewProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        SearchResultDto<ProductDto> result = searchService.getNewProducts(page, size);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "New products retrieved successfully",
            result
        ));
    }
    
    @GetMapping("/products/bestsellers")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<SearchResultDto<ProductDto>>> getBestsellerProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        SearchResultDto<ProductDto> result = searchService.getBestsellerProducts(page, size);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Bestseller products retrieved successfully",
            result
        ));
    }
    
    @GetMapping("/products/on-sale")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<SearchResultDto<ProductDto>>> getOnSaleProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        SearchResultDto<ProductDto> result = searchService.getOnSaleProducts(page, size);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "On-sale products retrieved successfully",
            result
        ));
    }
    
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> testSearch() {
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Search API is working",
            "Search controller is accessible"
        ));
    }
    
    @GetMapping("/test-products")
    public ResponseEntity<ApiResponse<String>> testProducts() {
        try {
            // Test search with empty criteria
            var searchDto = new org.example.trangsuc_js.dto.search.ProductSearchDto();
            searchDto.setPage(0);
            searchDto.setSize(5);
            var result = searchService.searchProducts(searchDto);
            
            return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Products test successful",
                "Found " + result.getTotalElements() + " products"
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(
                false,
                "Products test failed: " + e.getMessage(),
                e.toString()
            ));
        }
    }
}
