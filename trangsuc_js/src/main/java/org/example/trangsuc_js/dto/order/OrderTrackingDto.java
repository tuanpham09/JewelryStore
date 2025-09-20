package org.example.trangsuc_js.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingDto {
    private Long orderId;
    private String orderNumber;
    private String status;
    private String statusDescription;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    
    // Customer info
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    
    // Shipping info
    private String shippingAddress;
    private String shippingMethod;
    private String trackingNumber;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
    
    // Payment info
    private String paymentMethod;
    private String paymentStatus;
    private String paymentReference;
    private LocalDateTime paidAt;
    
    // Order items
    private List<OrderItemTrackingDto> items;
    
    // Tracking history
    private List<OrderStatusHistoryDto> statusHistory;
}
