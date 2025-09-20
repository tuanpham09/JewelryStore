package org.example.trangsuc_js.repository;


import org.example.trangsuc_js.entity.Order;
import org.example.trangsuc_js.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    Optional<Order> findByOrderNumber(String orderNumber);
}

