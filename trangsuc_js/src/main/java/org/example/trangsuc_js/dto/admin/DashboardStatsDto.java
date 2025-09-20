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

@Data
@NoArgsConstructor
@AllArgsConstructor
class TopProductDto {
    private Long productId;
    private String productName;
    private String productThumbnail;
    private Long totalSold;
    private BigDecimal totalRevenue;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TopCustomerDto {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long totalOrders;
    private BigDecimal totalSpent;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RevenueChartDto {
    private String date;
    private BigDecimal revenue;
    private Long orders;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class OrderStatusDto {
    private String status;
    private Long count;
    private BigDecimal totalValue;
}
