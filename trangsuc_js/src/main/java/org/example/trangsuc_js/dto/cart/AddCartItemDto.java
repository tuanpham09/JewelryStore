package org.example.trangsuc_js.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddCartItemDto {
    private Long productId;
    private Integer quantity;
    private String sizeValue; // Optional - có thể null
    private String colorValue; // Optional - có thể null
}
