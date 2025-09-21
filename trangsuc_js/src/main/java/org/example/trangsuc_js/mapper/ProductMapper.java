package org.example.trangsuc_js.mapper;


import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.entity.Category;
import org.example.trangsuc_js.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setSlug(p.getSlug());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setThumbnail(p.getThumbnail());
        dto.setCategoryName(p.getCategory() != null ? p.getCategory().getName() : null);
        
        // Tính average rating và review count
        if (p.getReviews() != null && !p.getReviews().isEmpty()) {
            double avgRating = p.getReviews().stream()
                    .mapToInt(r -> r.getRating())
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
            dto.setReviewCount(p.getReviews().size());
        }
        
        return dto;
    }

    public static Product toEntity(ProductUpsertDto dto, Category category) {
        Product p = new Product();
        p.setName(dto.getName());
        p.setSlug(dto.getSlug());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setThumbnail(dto.getThumbnail());
        p.setCategory(category);
        return p;
    }

    public static void updateEntity(Product p, ProductUpsertDto dto, Category category) {
        p.setName(dto.getName());
        p.setSlug(dto.getSlug());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setThumbnail(dto.getThumbnail());
        p.setCategory(category);
    }
}
