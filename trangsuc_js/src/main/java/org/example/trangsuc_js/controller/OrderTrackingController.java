package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.order.CancelOrderDto;
import org.example.trangsuc_js.dto.order.OrderTrackingDto;
import org.example.trangsuc_js.service.OrderTrackingService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderTrackingController {
    
    private final OrderTrackingService orderTrackingService;
    
    @GetMapping("/track/{orderId}")
    public ResponseEntity<ApiResponse<OrderTrackingDto>> trackOrder(@PathVariable Long orderId) {
        OrderTrackingDto tracking = orderTrackingService.trackOrder(orderId);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Order tracking retrieved successfully",
            tracking
        ));
    }
    
    @GetMapping("/track/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderTrackingDto>> trackOrderByNumber(@PathVariable String orderNumber) {
        OrderTrackingDto tracking = orderTrackingService.trackOrderByNumber(orderNumber);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Order tracking retrieved successfully",
            tracking
        ));
    }
    
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderTrackingDto>>> getMyOrders() {
        List<OrderTrackingDto> orders = orderTrackingService.getMyOrders();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Orders retrieved successfully",
            orders
        ));
    }
    
    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<OrderTrackingDto>> cancelOrder(
            @Valid @RequestBody CancelOrderDto cancelDto) {
        OrderTrackingDto tracking = orderTrackingService.cancelOrder(cancelDto);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Order cancelled successfully",
            tracking
        ));
    }
    
    @PostMapping("/{orderId}/return")
    public ResponseEntity<ApiResponse<OrderTrackingDto>> requestReturn(
            @PathVariable Long orderId,
            @RequestParam String reason) {
        OrderTrackingDto tracking = orderTrackingService.requestReturn(orderId, reason);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Return request submitted successfully",
            tracking
        ));
    }
}
