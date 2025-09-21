package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    private Long productId;
    private String productName;
    private String productThumbnail;
    private Integer currentStock;
    private Integer minStock;
    private Integer maxStock;
    private String status; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK
    private String categoryName;
    private Double averageRating;
    private Integer reviewCount;
}
