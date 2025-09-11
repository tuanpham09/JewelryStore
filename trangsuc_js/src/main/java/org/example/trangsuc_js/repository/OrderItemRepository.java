package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
