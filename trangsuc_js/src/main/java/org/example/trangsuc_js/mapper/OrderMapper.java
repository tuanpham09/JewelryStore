package org.example.trangsuc_js.mapper;


import org.example.trangsuc_js.dto.order.OrderDto;
import org.example.trangsuc_js.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto toDto(Order o) {
        List<OrderDto.OrderItemDto> items = o.getItems().stream()
                .map(oi -> {
                    OrderDto.OrderItemDto itemDto = new OrderDto.OrderItemDto();
                    itemDto.setProductId(oi.getProduct().getId());
                    itemDto.setProductName(oi.getProduct().getName());
                    itemDto.setQuantity(oi.getQuantity());
                    itemDto.setUnitPrice(oi.getPrice());
                    itemDto.setSubtotal(oi.getSubtotal());
                    itemDto.setSizeValue(oi.getSizeValue());
                    itemDto.setColorValue(oi.getColorValue());
                    return itemDto;
                })
                .collect(Collectors.toList());
        return new OrderDto(o.getId(), o.getOrderNumber(), o.getTotal(), o.getCreatedAt(), items);
    }
}
