package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    List<Wishlist> findByUserId(Long userId);
    
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);
    
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    
    void deleteByUserIdAndProductId(Long userId, Long productId);
    
    @Query("SELECT w FROM Wishlist w WHERE w.product.id = :productId")
    List<Wishlist> findByProductId(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.product.id = :productId")
    Long countByProductId(@Param("productId") Long productId);
}
