package org.example.trangsuc_js.controller;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.cart.AddCartItemDto;
import org.example.trangsuc_js.dto.cart.CartDto;
import org.example.trangsuc_js.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartDto getMyCart() {
        return cartService.getMyCart();
    }

    @PostMapping("/items")
    public CartDto addItem(@RequestBody AddCartItemDto dto) {
        return cartService.addItem(dto);
    }

    @DeleteMapping("/items/{productId}")
    public CartDto removeItem(@PathVariable Long productId) {
        return cartService.removeItem(productId);
    }
}
