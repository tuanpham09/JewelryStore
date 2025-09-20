package org.example.trangsuc_js.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSizeDto {
    private Long id;
    private String size;
    private Integer stock;
    private Boolean isActive;
    
    public ProductSizeDto() {}
    
    public ProductSizeDto(String size, Integer stock) {
        this.size = size;
        this.stock = stock;
        this.isActive = true;
    }
}