package org.example.trangsuc_js.repository;


import org.example.trangsuc_js.entity.Cart;
import org.example.trangsuc_js.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
