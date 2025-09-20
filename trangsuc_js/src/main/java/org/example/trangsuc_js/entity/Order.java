package org.example.trangsuc_js.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String orderNumber; // Mã đơn hàng duy nhất
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "shipping_fee", precision = 10, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
    
    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    // Thông tin giao hàng
    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;
    
    @Column(name = "billing_address", columnDefinition = "TEXT")
    private String billingAddress;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_phone")
    private String customerPhone;
    
    @Column(name = "customer_email")
    private String customerEmail;
    
    // Thông tin thanh toán
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    
    @Column(name = "payment_status")
    private String paymentStatus = "PENDING";
    
    @Column(name = "payment_reference")
    private String paymentReference;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    // Ghi chú
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    // Enum cho Order Status
    public enum OrderStatus {
        PENDING, PAID, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED
    }
    
    // Enum cho Payment Method
    public enum PaymentMethod {
        COD, BANK_TRANSFER, CREDIT_CARD, PAYPAL, VNPAY, MOMO
    }
}

