package org.example.trangsuc_js.dto.report;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
public class ReportDto {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalProductsSold;
}
