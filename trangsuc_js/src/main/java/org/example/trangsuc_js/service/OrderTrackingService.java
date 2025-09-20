package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.order.CancelOrderDto;
import org.example.trangsuc_js.dto.order.OrderTrackingDto;

import java.util.List;

public interface OrderTrackingService {
    OrderTrackingDto trackOrder(Long orderId);
    OrderTrackingDto trackOrderByNumber(String orderNumber);
    List<OrderTrackingDto> getMyOrders();
    OrderTrackingDto cancelOrder(CancelOrderDto cancelDto);
    OrderTrackingDto requestReturn(Long orderId, String reason);
}
