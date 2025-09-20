package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    
    Optional<Promotion> findByCode(String code);
    
    List<Promotion> findByActiveTrue();
    
    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.startDate <= :now AND p.endDate >= :now")
    List<Promotion> findActivePromotions(@Param("now") LocalDateTime now);
    
    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.startDate <= :now AND p.endDate >= :now AND p.code = :code")
    Optional<Promotion> findActivePromotionByCode(@Param("code") String code, @Param("now") LocalDateTime now);
}
