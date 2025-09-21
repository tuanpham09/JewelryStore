package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.cart.AddCartItemDto;
import org.example.trangsuc_js.dto.cart.CartDto;
import org.example.trangsuc_js.dto.cart.UpdateCartItemDto;

public interface CartService {
    CartDto getMyCart();
    CartDto addItem(AddCartItemDto dto);
    CartDto removeItem(Long productId, String sizeValue, String colorValue);
    CartDto updateItem(Long productId, String sizeValue, String colorValue, UpdateCartItemDto dto);
    CartDto clearCart();
}
