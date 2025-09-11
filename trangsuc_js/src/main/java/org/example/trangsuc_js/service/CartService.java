package org.example.trangsuc_js.service;


import org.example.trangsuc_js.dto.cart.AddCartItemDto;
import org.example.trangsuc_js.dto.cart.CartDto;

public interface CartService {
    CartDto getMyCart();
    CartDto addItem(AddCartItemDto dto);
    CartDto removeItem(Long productId);
}
