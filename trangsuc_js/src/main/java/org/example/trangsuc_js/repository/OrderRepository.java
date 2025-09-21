package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.Order;
import org.example.trangsuc_js.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findByUser(User user);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.payment WHERE o.id = :id")
    Optional<Order> findByIdWithItemsAndPayment(@Param("id") Long id);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.payment WHERE o.orderNumber = :orderNumber")
    Optional<Order> findByOrderNumberWithItemsAndPayment(@Param("orderNumber") String orderNumber);
    
    // Methods for admin service
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);
    List<Order> findByStatus(Order.OrderStatus status);
}