package org.example.trangsuc_js.repository;


import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.entity.Review;
import org.example.trangsuc_js.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    Optional<Review> findByUserAndProduct(User user, Product product);
}
