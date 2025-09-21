package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    
    List<ProductImage> findByProductId(Long productId);
    
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);
    
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);
    
    List<ProductImage> findByProductIdAndIsPrimaryFalse(Long productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId ORDER BY pi.isPrimary DESC, pi.sortOrder ASC")
    List<ProductImage> findByProductIdOrderByPrimaryAndSortOrder(@Param("productId") Long productId);
    
    @Query("SELECT COUNT(pi) FROM ProductImage pi WHERE pi.product.id = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.isPrimary = true")
    Optional<ProductImage> findPrimaryImageByProductId(@Param("productId") Long productId);
}
