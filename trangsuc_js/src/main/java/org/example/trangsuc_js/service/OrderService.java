package org.example.trangsuc_js.service;


import org.example.trangsuc_js.dto.order.CheckoutDto;
import org.example.trangsuc_js.dto.order.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto checkout(CheckoutDto dto);
    List<OrderDto> myOrders();
}
