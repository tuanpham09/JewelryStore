package org.example.trangsuc_js.dto.order;

import lombok.Data;
import org.example.trangsuc_js.entity.Order;
import org.example.trangsuc_js.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String notes;
    private List<OrderItemDto> items;
    private PaymentDto payment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for order tracking
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private LocalDateTime deliveredAt;
    private LocalDateTime paidAt;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentReference;
    
    // Constructors
    public OrderDto() {}
    
    public OrderDto(Long id, String orderNumber, BigDecimal totalAmount, LocalDateTime createdAt, List<OrderItemDto> items) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.items = items;
    }
    
    // Additional setter methods
    public void setTotal(BigDecimal total) {
        this.totalAmount = total;
    }
    
    @Data
    public static class OrderItemDto {
        private Long id;
        private Long productId;
        private String productName;
        private String productImage;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        private String sizeValue;
        private String colorValue;
    }
    
    @Data
    public static class PaymentDto {
        private Long id;
        private String paymentMethod;
        private BigDecimal amount;
        private Payment.PaymentStatus status;
        private String transactionId;
        private String payosPaymentId;
        private String payosPaymentUrl;
        private String payosQrCode;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime paidAt;
    }
}