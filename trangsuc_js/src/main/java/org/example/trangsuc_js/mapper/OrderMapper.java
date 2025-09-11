package org.example.trangsuc_js.mapper;


import org.example.trangsuc_js.dto.order.OrderDto;
import org.example.trangsuc_js.dto.order.OrderItemDto;
import org.example.trangsuc_js.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto toDto(Order o) {
        List<OrderItemDto> items = o.getItems().stream()
                .map(oi -> new OrderItemDto(
                        oi.getProduct().getId(),
                        oi.getProduct().getName(),
                        oi.getQuantity(),
                        oi.getPrice()))
                .collect(Collectors.toList());
        return new OrderDto(o.getId(), o.getStatus(), o.getTotal(), o.getCreatedAt(), items);
    }
}
