package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductSizeDto;
import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.entity.ProductSize;
import org.example.trangsuc_js.repository.ProductRepository;
import org.example.trangsuc_js.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        // Fetch product with category first
        Product product = productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Then fetch reviews and sizes separately
        Product productWithReviews = productRepository.findByIdWithReviews(id).orElse(product);
        Product productWithSizes = productRepository.findByIdWithSizes(id).orElse(product);
        
        // Merge the data
        if (productWithReviews.getReviews() != null) {
            product.setReviews(productWithReviews.getReviews());
        }
        if (productWithSizes.getSizes() != null) {
            product.setSizes(productWithSizes.getSizes());
        }
        
        return toProductDto(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAllWithCategory();
        return products.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findAllWithCategory().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
        
        return products.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getRelatedProducts(Long productId, int limit) {
        Product currentProduct = productRepository.findByIdWithCategory(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        List<Product> relatedProducts = productRepository.findAllWithCategory().stream()
                .filter(p -> !p.getId().equals(productId))
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(currentProduct.getCategory().getId()))
                .limit(limit)
                .collect(Collectors.toList());
        
        return relatedProducts.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ProductDto incrementViewCount(Long productId) {
        Product product = productRepository.findByIdWithCategory(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        
        return toProductDto(product);
    }
    
    private ProductDto toProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setShortDescription(product.getShortDescription());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setCurrentPrice(product.getCurrentPrice());
        dto.setStock(product.getStock());
        dto.setMinStock(product.getMinStock());
        dto.setMaxStock(product.getMaxStock());
        dto.setSku(product.getSku());
        dto.setBarcode(product.getBarcode());
        dto.setWeight(product.getWeight());
        dto.setDimensions(product.getDimensions());
        dto.setMaterial(product.getMaterial());
        dto.setColor(product.getColor());
        dto.setBrand(product.getBrand());
        dto.setOrigin(product.getOrigin());
        dto.setWarrantyPeriod(product.getWarrantyPeriod());
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setIsNew(product.getIsNew());
        dto.setIsBestseller(product.getIsBestseller());
        dto.setIsOnSale(product.isOnSale());
        dto.setDiscountPercentage(product.getDiscountPercentage());
        dto.setIsLowStock(product.isLowStock());
        dto.setIsOutOfStock(product.isOutOfStock());
        dto.setViewCount(product.getViewCount());
        dto.setSoldCount(product.getSoldCount());
        dto.setMetaTitle(product.getMetaTitle());
        dto.setMetaDescription(product.getMetaDescription());
        dto.setMetaKeywords(product.getMetaKeywords());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setThumbnail(product.getThumbnail());
        dto.setPrimaryImageUrl(product.getPrimaryImageUrl());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
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
        
        // Convert sizes
        if (product.getSizes() != null) {
            List<ProductSizeDto> sizeDtos = product.getSizes().stream()
                    .filter(ProductSize::getIsActive)
                    .map(this::toProductSizeDto)
                    .collect(Collectors.toList());
            dto.setSizes(sizeDtos);
        }
        
        return dto;
    }
    
    private ProductSizeDto toProductSizeDto(ProductSize size) {
        ProductSizeDto dto = new ProductSizeDto();
        dto.setId(size.getId());
        dto.setSize(size.getSize());
        dto.setStock(size.getStock());
        dto.setIsActive(size.getIsActive());
        return dto;
    }
}
