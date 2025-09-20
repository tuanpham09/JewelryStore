package org.example.trangsuc_js.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;
    
    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(name = "min_stock")
    private Integer minStock = 5;

    @Column(name = "max_stock")
    private Integer maxStock = 1000;
    
    @Column(name = "sku", unique = true)
    private String sku;
    
    @Column(name = "barcode")
    private String barcode;
    
    @Column(name = "weight")
    private BigDecimal weight;
    
    @Column(name = "dimensions")
    private String dimensions; // "L x W x H"
    
    @Column(name = "material")
    private String material;
    
    @Column(name = "color")
    private String color;
    
    @Column(name = "brand")
    private String brand;
    
    @Column(name = "origin")
    private String origin;
    
    @Column(name = "warranty_period")
    private Integer warrantyPeriod; // months
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @Column(name = "is_new")
    private Boolean isNew = true;
    
    @Column(name = "is_bestseller")
    private Boolean isBestseller = false;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "sold_count")
    private Long soldCount = 0L;
    
    @Column(name = "meta_title")
    private String metaTitle;
    
    @Column(name = "meta_description", columnDefinition = "TEXT")
    private String metaDescription;
    
    @Column(name = "meta_keywords")
    private String metaKeywords;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Legacy field - sẽ được thay thế bởi primary image
    @Column(name = "thumbnail")
    private String thumbnail;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
    
    // Helper methods
    public ProductImage getPrimaryImage() {
        return images.stream()
                .filter(ProductImage::getIsPrimary)
                .findFirst()
                .orElse(null);
    }
    
    public String getPrimaryImageUrl() {
        ProductImage primaryImage = getPrimaryImage();
        return primaryImage != null ? primaryImage.getImageUrl() : thumbnail;
    }
    
    public void setPrimaryImage(ProductImage image) {
        // Set all images as non-primary first
        images.forEach(img -> img.setIsPrimary(false));
        // Set the selected image as primary
        image.setIsPrimary(true);
    }
    
    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
        if (images.size() == 1) {
            image.setIsPrimary(true);
        }
    }
    
    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
        if (image.getIsPrimary() && !images.isEmpty()) {
            images.get(0).setIsPrimary(true);
        }
    }
    
    public BigDecimal getCurrentPrice() {
        return salePrice != null ? salePrice : price;
    }
    
    public boolean isOnSale() {
        return salePrice != null && originalPrice != null && salePrice.compareTo(originalPrice) < 0;
    }
    
    public BigDecimal getDiscountPercentage() {
        if (!isOnSale()) return BigDecimal.ZERO;
        return price.subtract(salePrice)
                .divide(price, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
    
    public boolean isLowStock() {
        return stock <= minStock;
    }
    
    public boolean isOutOfStock() {
        return stock <= 0;
    }
}
