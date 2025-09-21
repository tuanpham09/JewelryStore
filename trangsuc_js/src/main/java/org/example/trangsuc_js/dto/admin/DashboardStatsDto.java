package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private BigDecimal monthlyRevenue;
    private Long totalOrders;
    private Long todayOrders;
    private Long monthlyOrders;
    private Long totalCustomers;
    private Long newCustomersToday;
    private Long totalProducts;
    private Long lowStockProducts;
    private Long outOfStockProducts;
    private Long pendingOrders;
    private Long activePromotions;
    private List<TopProductDto> topSellingProducts;
    private List<TopCustomerDto> topCustomers;
    private List<RevenueChartDto> revenueChart;
    private List<OrderStatusDto> orderStatusStats;
}

