package org.example.trangsuc_js.dto.cart;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddCartItemDto {
    private Long productId;
    private int quantity;
}
