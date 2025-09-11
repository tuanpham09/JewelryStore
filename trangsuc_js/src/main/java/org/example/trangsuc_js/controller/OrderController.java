package org.example.trangsuc_js.controller;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.order.CheckoutDto;
import org.example.trangsuc_js.dto.order.OrderDto;
import org.example.trangsuc_js.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public OrderDto checkout(@RequestBody CheckoutDto dto) {
        return orderService.checkout(dto);
    }

    @GetMapping("/mine")
    public List<OrderDto> myOrders() {
        return orderService.myOrders();
    }
}
