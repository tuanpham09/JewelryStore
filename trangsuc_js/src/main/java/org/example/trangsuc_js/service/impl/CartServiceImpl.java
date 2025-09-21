package org.example.trangsuc_js.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trangsuc_js.dto.cart.AddCartItemDto;
import org.example.trangsuc_js.dto.cart.CartDto;
import org.example.trangsuc_js.dto.cart.CartItemDto;
import org.example.trangsuc_js.dto.cart.UpdateCartItemDto;
import org.example.trangsuc_js.entity.*;
import org.example.trangsuc_js.repository.*;
import org.example.trangsuc_js.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CartRepository cartRepo;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public CartDto getMyCart() {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepo.save(c);
        });

        // Sử dụng quan hệ @OneToMany để lấy items
        List<CartItem> items = cart.getItems() != null ? cart.getItems() : new ArrayList<>();

        log.info("Cart items loaded: {}", items.size());
        
        // Cập nhật total và count
        cart.calculateTotal();
        cartRepo.save(cart);

        return toDto(items);
    }

    @Override
    @Transactional
    public CartDto addItem(AddCartItemDto dto) {
        log.info("Adding item to cart: productId={}, quantity={}, sizeValue={}, colorValue={}", 
                dto.getProductId(), dto.getQuantity(), dto.getSizeValue(), dto.getColorValue());
        
        User user = getCurrentUser();
        log.info("Current user: {}", user.getEmail());
        
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepo.save(c);
        });
        log.info("Cart ID: {}", cart.getId());

        Product product = productRepo.findById(dto.getProductId()).orElseThrow();
        log.info("Product found: {}", product.getName());

        // Tìm item theo productId, sizeValue, colorValue để phân biệt variant
        log.info("Searching for existing cart item: cartId={}, productId={}, sizeValue={}, colorValue={}", 
                cart.getId(), product.getId(), dto.getSizeValue(), dto.getColorValue());
        
        // Sử dụng quan hệ @OneToMany để tìm item trong cart
        CartItem existingItem = null;
        if (cart.getItems() != null) {
            existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()) &&
                                   Objects.equals(item.getSizeValue(), dto.getSizeValue()) &&
                                   Objects.equals(item.getColorValue(), dto.getColorValue()))
                    .findFirst()
                    .orElse(null);
        }
        
        log.info("Searching in cart items: cartId={}, productId={}, sizeValue={}, colorValue={}", 
                cart.getId(), product.getId(), dto.getSizeValue(), dto.getColorValue());
        
        if (existingItem != null) {
            // Item đã tồn tại (cùng variant) - cộng dồn quantity
            log.info("Found existing item: id={}, quantity={}, sizeValue={}, colorValue={}", 
                    existingItem.getId(), existingItem.getQuantity(), existingItem.getSizeValue(), existingItem.getColorValue());
            
            Integer currentQuantity = existingItem.getQuantity();
            if (currentQuantity == null) {
                currentQuantity = 0;
            }
            existingItem.setQuantity(currentQuantity + dto.getQuantity());
            log.info("Updating existing cart item: id={}, new quantity={}", existingItem.getId(), existingItem.getQuantity());
        } else {
            // Item mới (variant mới) - tạo mới và thêm vào cart
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setSizeValue(dto.getSizeValue());
            newItem.setColorValue(dto.getColorValue());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(dto.getQuantity());
            
            // Thêm vào cart sử dụng helper method
            cart.addItem(newItem);
            
            log.info("Creating new cart item variant: productId={}, size={}, color={}, quantity={}", 
                    product.getId(), dto.getSizeValue(), dto.getColorValue(), dto.getQuantity());
        }
        
        // Lưu cart (sẽ cascade save items)
        cartRepo.save(cart);

        return getMyCart();
    }

    @Override
    @Transactional
    public CartDto removeItem(Long productId, String sizeValue, String colorValue) {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseThrow();
        
        // Sử dụng quan hệ @OneToMany để tìm item trong cart
        CartItem itemToRemove = null;
        if (cart.getItems() != null) {
            itemToRemove = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId) &&
                                   Objects.equals(item.getSizeValue(), sizeValue) &&
                                   Objects.equals(item.getColorValue(), colorValue))
                    .findFirst()
                    .orElse(null);
        }
        
        if (itemToRemove != null) {
            log.info("Removing cart item variant: productId={}, size={}, color={}", 
                    productId, sizeValue, colorValue);
            
            // Sử dụng helper method để remove item
            cart.removeItem(itemToRemove);
            cartRepo.save(cart);
        }
        
        return getMyCart();
    }

    @Override
    @Transactional
    public CartDto updateItem(Long productId, String sizeValue, String colorValue, UpdateCartItemDto dto) {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseThrow();
        
        // Sử dụng quan hệ @OneToMany để tìm item trong cart
        CartItem itemToUpdate = null;
        if (cart.getItems() != null) {
            itemToUpdate = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId) &&
                                   Objects.equals(item.getSizeValue(), sizeValue) &&
                                   Objects.equals(item.getColorValue(), colorValue))
                    .findFirst()
                    .orElse(null);
        }
        
        if (itemToUpdate != null) {
            log.info("Updating cart item variant: productId={}, size={}, color={}, newQuantity={}", 
                    productId, sizeValue, colorValue, dto.getQuantity());
            
            itemToUpdate.setQuantity(dto.getQuantity());
            
            // Cập nhật total và count
            cart.calculateTotal();
            cartRepo.save(cart);
        }
        
        return getMyCart();
    }

    @Override
    @Transactional
    public CartDto clearCart() {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseThrow();
        
        // Sử dụng quan hệ @OneToMany để clear items
        if (cart.getItems() != null) {
            cart.getItems().clear();
            cart.calculateTotal();
            cartRepo.save(cart);
        }
        
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
                    dto.setSizeValue(i.getSizeValue());
                    dto.setColorValue(i.getColorValue());
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
