package org.example.trangsuc_js.dto.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private BigDecimal currentPrice;
    private Integer stock;
    private Integer minStock;
    private Integer maxStock;
    private String sku;
    private String barcode;
    private BigDecimal weight;
    private String dimensions;
    private String material;
    private String color;
    private String brand;
    private String origin;
    private Integer warrantyPeriod;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isNew;
    private Boolean isBestseller;
    private Boolean isOnSale;
    private BigDecimal discountPercentage;
    private Boolean isLowStock;
    private Boolean isOutOfStock;
    private Long viewCount;
    private Long soldCount;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Legacy field
    private String thumbnail;
    private String primaryImageUrl;
    
    // Category info
    private Long categoryId;
    private String categoryName;
    
    // Rating info
    private Double averageRating = 0.0;
    private Integer reviewCount = 0;
    
    // Images
    private List<ProductImageDto> images;
    
    // Sizes
    private List<ProductSizeDto> sizes;
}
