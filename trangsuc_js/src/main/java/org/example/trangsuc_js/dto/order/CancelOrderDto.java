package org.example.trangsuc_js.dto.order;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CancelOrderDto {
    @NotNull(message = "Order ID không được để trống")
    private Long orderId;
    
    @NotBlank(message = "Lý do hủy đơn không được để trống")
    private String reason;
    
    private String note;
}
