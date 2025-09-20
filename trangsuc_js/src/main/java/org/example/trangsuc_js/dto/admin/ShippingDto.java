package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDto {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private String shippingMethod; // GHTK, GHN, VIETTEL_POST, J&T
    private String trackingNumber;
    private String status; // PENDING, PICKED_UP, IN_TRANSIT, DELIVERED, FAILED
    private String fromAddress;
    private String toAddress;
    private String customerName;
    private String customerPhone;
    private BigDecimal shippingFee;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime actualDelivery;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
}
