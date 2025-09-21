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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="order_number", unique = true, nullable = false)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name="total_amount", precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10,2) NOT NULL DEFAULT 0.00")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name="shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name="shipping_phone")
    private String shippingPhone;

    @Column(name="shipping_name")
    private String shippingName;

    @Column(name="notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Additional fields for order tracking
    @Column(name="cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name="cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name="delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name="paid_at")
    private LocalDateTime paidAt;

    // Customer information
    @Column(name="customer_name")
    private String customerName;

    @Column(name="customer_phone")
    private String customerPhone;

    @Column(name="customer_email")
    private String customerEmail;

    // Payment information
    @Column(name="payment_method")
    private String paymentMethod;

    @Column(name="payment_status")
    private String paymentStatus;

    @Column(name="payment_reference")
    private String paymentReference;

    // Additional fields for admin service
    @Column(name="subtotal", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name="shipping_fee", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Column(name="discount_amount", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name="billing_address", columnDefinition = "TEXT")
    private String billingAddress;

    // Helper methods
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public void calculateTotal() {
        if (items != null && !items.isEmpty()) {
            this.totalAmount = items.stream()
                    .map(OrderItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalAmount = BigDecimal.ZERO;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getTotal() {
        return this.totalAmount;
    }

    public void setTotal(BigDecimal total) {
        this.totalAmount = total;
    }

    public enum OrderStatus {
        PENDING,        // Chờ thanh toán
        PAID,           // Đã thanh toán
        PROCESSING,     // Đang xử lý
        SHIPPED,        // Đã giao hàng
        DELIVERED,      // Đã nhận hàng
        CANCELLED,      // Đã hủy
        REFUNDED,       // Đã hoàn tiền
        CONFIRMED
    }
}