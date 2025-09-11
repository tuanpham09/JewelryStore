package org.example.trangsuc_js.dto.product;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ProductUpsertDto {
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String thumbnail;
    private Long categoryId;
}
