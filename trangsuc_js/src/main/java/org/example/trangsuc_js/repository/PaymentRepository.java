package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPayosPaymentId(String payosPaymentId);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByOrderId(Long orderId);
}
