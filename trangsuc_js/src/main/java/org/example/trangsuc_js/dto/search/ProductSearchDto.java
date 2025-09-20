package org.example.trangsuc_js.dto.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDto {
    private String query;
    private String keyword;
    private Long categoryId;
    private List<Long> categoryIds;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String brand;
    private List<String> brands;
    private String material;
    private List<String> materials;
    private String color;
    private List<String> colors;
    private String sortBy;
    private String sortOrder;
    private Integer page = 0;
    private Integer size = 12;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isNew;
    private Boolean isBestseller;
    private Boolean isOnSale;
}
