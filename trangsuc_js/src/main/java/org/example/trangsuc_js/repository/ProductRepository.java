package org.example.trangsuc_js.repository;


import org.example.trangsuc_js.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    List<Product> findAllWithCategory();
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.reviews LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithReviews(@Param("id") Long id);
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.sizes LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithSizes(@Param("id") Long id);
}
