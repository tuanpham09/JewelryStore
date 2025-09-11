package org.example.trangsuc_js.mapper;


import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.entity.Category;
import org.example.trangsuc_js.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product p) {
        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStock(),
                p.getCategory() != null ? p.getCategory().getName() : null
        );
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
