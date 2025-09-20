package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.wishlist.WishlistDto;
import org.example.trangsuc_js.dto.wishlist.WishlistItemDto;

import java.util.List;

public interface WishlistService {
    List<WishlistItemDto> getWishlist();
    WishlistItemDto addToWishlist(Long productId);
    void removeFromWishlist(Long productId);
    boolean isInWishlist(Long productId);
    List<WishlistItemDto> getPriceDropNotifications();
}
