package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.CustomerAddress;
import org.example.trangsuc_js.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByUser(User user);
    List<CustomerAddress> findByUserId(Long userId);
    Optional<CustomerAddress> findByUserIdAndIsDefaultTrue(Long userId);
    Optional<CustomerAddress> findByUserIdAndId(Long userId, Long id);
}
