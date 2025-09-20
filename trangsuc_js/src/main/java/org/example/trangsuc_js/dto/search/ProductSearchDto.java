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
    private String keyword;
    private List<Long> categoryIds;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> materials;
    private List<String> colors;
    private List<String> brands;
    private String sortBy; // price_asc, price_desc, rating_desc, popularity_desc, newest
    private Integer page = 0;
    private Integer size = 20;
    private Boolean isActive = true;
    private Boolean isFeatured;
    private Boolean isNew;
    private Boolean isBestseller;
    private Boolean isOnSale;
}
