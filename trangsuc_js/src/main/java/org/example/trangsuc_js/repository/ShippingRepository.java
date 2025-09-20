package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    
    Optional<Shipping> findByOrderId(Long orderId);
    
    Optional<Shipping> findByTrackingNumber(String trackingNumber);
    
    List<Shipping> findByStatus(Shipping.ShippingStatus status);
    
    Page<Shipping> findByStatus(Shipping.ShippingStatus status, Pageable pageable);
    
    List<Shipping> findByShippingMethod(Shipping.ShippingMethod shippingMethod);
    
    Page<Shipping> findByShippingMethod(Shipping.ShippingMethod shippingMethod, Pageable pageable);
    
    @Query("SELECT s FROM Shipping s WHERE s.status = :status AND s.shippingMethod = :method")
    List<Shipping> findByStatusAndMethod(@Param("status") Shipping.ShippingStatus status, 
                                        @Param("method") Shipping.ShippingMethod method);
}
