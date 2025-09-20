package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.wishlist.WishlistItemDto;
import org.example.trangsuc_js.service.WishlistService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WishlistController {
    
    private final WishlistService wishlistService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<WishlistItemDto>>> getWishlist() {
        List<WishlistItemDto> wishlist = wishlistService.getWishlist();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Wishlist retrieved successfully",
            wishlist
        ));
    }
    
    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<WishlistItemDto>> addToWishlist(@PathVariable Long productId) {
        WishlistItemDto item = wishlistService.addToWishlist(productId);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Product added to wishlist successfully",
            item
        ));
    }
    
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> removeFromWishlist(@PathVariable Long productId) {
        wishlistService.removeFromWishlist(productId);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Product removed from wishlist successfully",
            null
        ));
    }
    
    @GetMapping("/{productId}/check")
    public ResponseEntity<ApiResponse<Boolean>> isInWishlist(@PathVariable Long productId) {
        boolean isInWishlist = wishlistService.isInWishlist(productId);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Wishlist status checked successfully",
            isInWishlist
        ));
    }
    
    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<List<WishlistItemDto>>> getPriceDropNotifications() {
        List<WishlistItemDto> notifications = wishlistService.getPriceDropNotifications();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Price drop notifications retrieved successfully",
            notifications
        ));
    }
}
