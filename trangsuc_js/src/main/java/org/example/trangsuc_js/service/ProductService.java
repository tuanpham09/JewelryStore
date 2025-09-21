package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.entity.Product;

import java.util.List;

public interface ProductService {
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts();
    List<ProductDto> getProductsByCategory(Long categoryId);
    List<ProductDto> getRelatedProducts(Long productId, int limit);
    ProductDto incrementViewCount(Long productId);
}
