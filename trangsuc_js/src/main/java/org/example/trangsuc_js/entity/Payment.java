package org.example.trangsuc_js.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @Column(name="payment_method", nullable = false)
    private String paymentMethod = "PAYOS";

    @Column(name="amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name="transaction_id")
    private String transactionId;

    @Column(name="payos_payment_id")
    private String payosPaymentId;

    @Column(name="payos_payment_url")
    private String payosPaymentUrl;

    @Column(name="payos_qr_code")
    private String payosQrCode;

    @Column(name="payos_checksum")
    private String payosChecksum;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name="paid_at")
    private LocalDateTime paidAt;

    public enum PaymentStatus {
        PENDING,        // Chờ thanh toán
        PROCESSING,     // Đang xử lý
        SUCCESS,        // Thanh toán thành công
        FAILED,         // Thanh toán thất bại
        CANCELLED,      // Đã hủy
        REFUNDED        // Đã hoàn tiền
    }
}
