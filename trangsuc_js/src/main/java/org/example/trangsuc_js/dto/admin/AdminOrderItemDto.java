package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private String productThumbnail;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal price;
    private BigDecimal discountAmount;
    private BigDecimal subtotal;
    private LocalDateTime createdAt;
}
