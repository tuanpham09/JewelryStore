package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomerDto {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long totalOrders;
    private BigDecimal totalSpent;
}
