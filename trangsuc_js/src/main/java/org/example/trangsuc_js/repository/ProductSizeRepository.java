package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    
    List<ProductSize> findByProductIdAndIsActiveTrue(Long productId);
    
    Optional<ProductSize> findByProductIdAndSizeAndIsActiveTrue(Long productId, String size);
    
    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.id = :productId AND ps.isActive = true ORDER BY ps.size")
    List<ProductSize> findActiveSizesByProductId(@Param("productId") Long productId);
    
    @Query("SELECT ps.stock FROM ProductSize ps WHERE ps.product.id = :productId AND ps.size = :size AND ps.isActive = true")
    Optional<Integer> findStockByProductIdAndSize(@Param("productId") Long productId, @Param("size") String size);
}