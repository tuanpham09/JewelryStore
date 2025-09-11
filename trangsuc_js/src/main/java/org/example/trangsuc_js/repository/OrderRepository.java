package org.example.trangsuc_js.repository;


import org.example.trangsuc_js.entity.Order;
import org.example.trangsuc_js.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

