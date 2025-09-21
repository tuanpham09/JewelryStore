package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDto {
    private Long id;
    private Long userId;
    private String orderNumber;
    private String status;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    
    // Customer info
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Address info
    private String shippingAddress;
    private String billingAddress;
    
    // Payment info
    private String paymentMethod;
    private String paymentStatus;
    private String paymentReference;
    private LocalDateTime paidAt;
    
    // Shipping info
    private String shippingMethod;
    private String trackingNumber;
    
    // Order items
    private List<AdminOrderItemDto> items;
    
    // Notes
    private String notes;
}
