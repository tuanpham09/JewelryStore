package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productThumbnail;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
