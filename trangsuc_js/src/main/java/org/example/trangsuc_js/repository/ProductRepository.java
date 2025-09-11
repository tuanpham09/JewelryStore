package org.example.trangsuc_js.repository;


import org.example.trangsuc_js.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
