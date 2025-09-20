package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.Cart;
import org.example.trangsuc_js.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    
    // Tìm item theo productId, sizeValue, colorValue (để phân biệt variant)
    // Sử dụng custom query để xử lý null values đúng cách
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId " +
           "AND (ci.sizeValue = :sizeValue OR (ci.sizeValue IS NULL AND :sizeValue IS NULL)) " +
           "AND (ci.colorValue = :colorValue OR (ci.colorValue IS NULL AND :colorValue IS NULL))")
    Optional<CartItem> findByCartIdAndProductIdAndSizeValueAndColorValue(
        @Param("cartId") Long cartId, 
        @Param("productId") Long productId, 
        @Param("sizeValue") String sizeValue, 
        @Param("colorValue") String colorValue);
    
    void deleteAllByCart(Cart cart);
}
