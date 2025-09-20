package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.wishlist.WishlistItemDto;
import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.entity.User;
import org.example.trangsuc_js.entity.Wishlist;
import org.example.trangsuc_js.repository.ProductRepository;
import org.example.trangsuc_js.repository.UserRepository;
import org.example.trangsuc_js.repository.WishlistRepository;
import org.example.trangsuc_js.service.WishlistService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    
    private final WishlistRepository wishlistRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WishlistItemDto> getWishlist() {
        User user = getCurrentUser();
        List<Wishlist> wishlistItems = wishlistRepo.findByUserId(user.getId());
        
        return wishlistItems.stream()
                .map(this::toWishlistItemDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public WishlistItemDto addToWishlist(Long productId) {
        User user = getCurrentUser();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check if already in wishlist
        Optional<Wishlist> existingWishlist = wishlistRepo.findByUserIdAndProductId(user.getId(), productId);
        if (existingWishlist.isPresent()) {
            throw new RuntimeException("Product already in wishlist");
        }
        
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        
        wishlistRepo.save(wishlist);
        return toWishlistItemDto(wishlist);
    }
    
    @Override
    @Transactional
    public void removeFromWishlist(Long productId) {
        User user = getCurrentUser();
        wishlistRepo.deleteByUserIdAndProductId(user.getId(), productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isInWishlist(Long productId) {
        User user = getCurrentUser();
        return wishlistRepo.existsByUserIdAndProductId(user.getId(), productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WishlistItemDto> getPriceDropNotifications() {
        User user = getCurrentUser();
        List<Wishlist> wishlistItems = wishlistRepo.findByUserId(user.getId());
        
        return wishlistItems.stream()
                .filter(item -> item.getProduct().isOnSale())
                .map(this::toWishlistItemDto)
                .collect(Collectors.toList());
    }
    
    private WishlistItemDto toWishlistItemDto(Wishlist wishlist) {
        Product product = wishlist.getProduct();
        WishlistItemDto dto = new WishlistItemDto();
        
        dto.setId(wishlist.getId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductSlug(product.getSlug());
        dto.setProductImage(product.getPrimaryImageUrl());
        dto.setCurrentPrice(product.getCurrentPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setIsOnSale(product.isOnSale());
        dto.setDiscountPercentage(product.getDiscountPercentage());
        dto.setIsInStock(!product.isOutOfStock());
        dto.setStock(product.getStock());
        dto.setAddedAt(wishlist.getCreatedAt());
        
        // Check for price drop
        if (product.isOnSale() && product.getOriginalPrice() != null) {
            dto.setHasPriceDrop(true);
            dto.setPriceDropAmount(product.getOriginalPrice().subtract(product.getCurrentPrice()));
        } else {
            dto.setHasPriceDrop(false);
            dto.setPriceDropAmount(null);
        }
        
        return dto;
    }
}
