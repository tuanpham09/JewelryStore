package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trangsuc_js.dto.cart.AddCartItemDto;
import org.example.trangsuc_js.dto.cart.CartDto;
import org.example.trangsuc_js.dto.cart.UpdateCartItemDto;
import org.example.trangsuc_js.service.CartService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ApiResponse<CartDto> getMyCart() {
        CartDto cart = cartService.getMyCart();
        return ApiResponse.success("Cart retrieved successfully", cart);
    }

    @PostMapping("/items")
    public ApiResponse<CartDto> addItem(@RequestBody AddCartItemDto dto) {
        log.info("Received addItem request: {}", dto);
        try {
            CartDto cart = cartService.addItem(dto);
            log.info("Successfully added item to cart. Cart ID: {}", cart.getId());
            return ApiResponse.success("Item added to cart successfully", cart);
        } catch (Exception e) {
            log.error("Error adding item to cart: ", e);
            throw e;
        }
    }

    @DeleteMapping("/items/{productId}")
    public ApiResponse<CartDto> removeItem(@PathVariable Long productId, 
                                         @RequestParam(required = false) String sizeValue,
                                         @RequestParam(required = false) String colorValue) {
        CartDto cart = cartService.removeItem(productId, sizeValue, colorValue);
        return ApiResponse.success("Item removed from cart successfully", cart);
    }

    @PutMapping("/items/{productId}")
    public ApiResponse<CartDto> updateItem(@PathVariable Long productId, 
                                         @RequestParam(required = false) String sizeValue,
                                         @RequestParam(required = false) String colorValue,
                                         @RequestBody UpdateCartItemDto dto) {
        CartDto cart = cartService.updateItem(productId, sizeValue, colorValue, dto);
        return ApiResponse.success("Item updated successfully", cart);
    }

    @DeleteMapping
    public ApiResponse<CartDto> clearCart() {
        CartDto cart = cartService.clearCart();
        return ApiResponse.success("Cart cleared successfully", cart);
    }

    @PostMapping("/sync")
    public ApiResponse<CartDto> syncCartFromLocalStorage(@RequestBody Object localCartItems) {
        CartDto cart = cartService.syncCartFromLocalStorage(localCartItems);
        return ApiResponse.success("Cart synced successfully", cart);
    }

    @GetMapping("/test")
    public ApiResponse<String> testCart() {
        log.info("Cart test endpoint called");
        return ApiResponse.success("Cart API is working", "OK");
    }

    @GetMapping("/auth-test")
    public ApiResponse<String> testAuth() {
        log.info("Auth test endpoint called");
        return ApiResponse.success("Authentication is working", "OK");
    }
}
