package org.example.trangsuc_js.dto.cart;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
public class CartItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
