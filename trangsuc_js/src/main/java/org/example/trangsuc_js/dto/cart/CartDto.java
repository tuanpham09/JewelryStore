package org.example.trangsuc_js.dto.cart;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class CartDto {
    private List<CartItemDto> items;
    private BigDecimal total;
}

