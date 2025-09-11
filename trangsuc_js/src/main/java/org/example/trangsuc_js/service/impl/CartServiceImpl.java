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
                .map(i -> new CartItemDto(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                ))
                .collect(Collectors.toList());

        BigDecimal total = dtoItems.stream()
                .map(CartItemDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartDto(dtoItems, total);
    }
}
