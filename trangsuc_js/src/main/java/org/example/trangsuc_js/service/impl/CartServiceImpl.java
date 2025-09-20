package org.example.trangsuc_js.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.cart.AddCartItemDto;
import org.example.trangsuc_js.dto.cart.CartDto;
import org.example.trangsuc_js.dto.cart.CartItemDto;
import org.example.trangsuc_js.entity.*;
import org.example.trangsuc_js.repository.*;
import org.example.trangsuc_js.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow();
    }

    @Override
    public CartDto getMyCart() {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepo.save(c);
        });

        List<CartItem> items = cartItemRepo.findAll()
                .stream().filter(i -> i.getCart().equals(cart)).toList();

        return toDto(items);
    }

    @Override
    public CartDto addItem(AddCartItemDto dto) {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepo.save(c);
        });

        Product product = productRepo.findById(dto.getProductId()).orElseThrow();

        CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(new CartItem());
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(item.getQuantity() + dto.getQuantity());
        item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        cartItemRepo.save(item);

        return getMyCart();
    }

    @Override
    public CartDto removeItem(Long productId) {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseThrow();
        CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId).orElseThrow();
        cartItemRepo.delete(item);
        return getMyCart();
    }

    private CartDto toDto(List<CartItem> items) {
        List<CartItemDto> dtoItems = items.stream()
                .map(i -> {
                    CartItemDto dto = new CartItemDto();
                    dto.setId(i.getId());
                    dto.setCartId(i.getCart().getId());
                    dto.setProductId(i.getProduct().getId());
                    dto.setProductName(i.getProduct().getName());
                    dto.setProductSku(i.getProduct().getSku());
                    dto.setProductImage(i.getProduct().getThumbnail());
                    dto.setUnitPrice(i.getPrice());
                    dto.setQuantity(i.getQuantity());
                    dto.setSubtotal(i.getSubtotal());
                    dto.setCreatedAt(i.getCreatedAt());
                    dto.setUpdatedAt(i.getUpdatedAt());
                    return dto;
                })
                .collect(Collectors.toList());

        BigDecimal total = dtoItems.stream()
                .map(CartItemDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartDto cartDto = new CartDto();
        cartDto.setItems(dtoItems);
        cartDto.setTotalAmount(total);
        cartDto.setItemCount(dtoItems.size());
        if (!items.isEmpty()) {
            cartDto.setId(items.get(0).getCart().getId());
            cartDto.setUserId(items.get(0).getCart().getUser().getId());
            cartDto.setCreatedAt(items.get(0).getCart().getCreatedAt());
            cartDto.setUpdatedAt(items.get(0).getCart().getUpdatedAt());
        }
        return cartDto;
    }
}
