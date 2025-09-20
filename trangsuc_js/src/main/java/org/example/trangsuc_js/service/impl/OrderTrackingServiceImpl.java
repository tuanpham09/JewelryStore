package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.order.*;
import org.example.trangsuc_js.entity.Order;
import org.example.trangsuc_js.entity.OrderItem;
import org.example.trangsuc_js.entity.Shipping;
import org.example.trangsuc_js.entity.User;
import org.example.trangsuc_js.repository.OrderRepository;
import org.example.trangsuc_js.repository.ShippingRepository;
import org.example.trangsuc_js.repository.UserRepository;
import org.example.trangsuc_js.service.OrderTrackingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderTrackingServiceImpl implements OrderTrackingService {
    
    private final OrderRepository orderRepo;
    private final ShippingRepository shippingRepo;
    private final UserRepository userRepo;
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderTrackingDto trackOrder(Long orderId) {
        User user = getCurrentUser();
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if user owns this order
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        return toOrderTrackingDto(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderTrackingDto trackOrderByNumber(String orderNumber) {
        User user = getCurrentUser();
        Order order = orderRepo.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if user owns this order
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        return toOrderTrackingDto(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrderTrackingDto> getMyOrders() {
        User user = getCurrentUser();
        List<Order> orders = orderRepo.findByUser(user);
        
        return orders.stream()
                .map(this::toOrderTrackingDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OrderTrackingDto cancelOrder(CancelOrderDto cancelDto) {
        User user = getCurrentUser();
        Order order = orderRepo.findById(cancelDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if user owns this order
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        // Check if order can be cancelled
        if (order.getStatus() == Order.OrderStatus.DELIVERED || 
            order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        // Cancel the order
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(cancelDto.getReason());
        order.setUpdatedAt(LocalDateTime.now());
        
        orderRepo.save(order);
        
        return toOrderTrackingDto(order);
    }
    
    @Override
    @Transactional
    public OrderTrackingDto requestReturn(Long orderId, String reason) {
        User user = getCurrentUser();
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if user owns this order
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        // Check if order can be returned
        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Only delivered orders can be returned");
        }
        
        // For now, just add a note. In production, you would create a return request
        order.setNotes((order.getNotes() != null ? order.getNotes() + "\n" : "") + 
                      "Return requested: " + reason + " at " + LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        orderRepo.save(order);
        
        return toOrderTrackingDto(order);
    }
    
    private OrderTrackingDto toOrderTrackingDto(Order order) {
        OrderTrackingDto dto = new OrderTrackingDto();
        dto.setOrderId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus().name());
        dto.setStatusDescription(getStatusDescription(order.getStatus()));
        dto.setTotal(order.getTotal());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setDeliveredAt(order.getDeliveredAt());
        dto.setCancelledAt(order.getCancelledAt());
        dto.setCancellationReason(order.getCancellationReason());
        
        // Customer info
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setCustomerEmail(order.getCustomerEmail());
        
        // Shipping info
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null);
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentReference(order.getPaymentReference());
        dto.setPaidAt(order.getPaidAt());
        
        // Order items
        dto.setItems(order.getItems().stream()
                .map(this::toOrderItemTrackingDto)
                .collect(Collectors.toList()));
        
        // Get shipping info if available
        shippingRepo.findByOrderId(order.getId()).ifPresent(shipping -> {
            dto.setShippingMethod(shipping.getShippingMethod().name());
            dto.setTrackingNumber(shipping.getTrackingNumber());
            dto.setEstimatedDelivery(shipping.getEstimatedDelivery());
            dto.setActualDelivery(shipping.getActualDelivery());
        });
        
        // Status history (simplified)
        dto.setStatusHistory(createStatusHistory(order));
        
        return dto;
    }
    
    private OrderItemTrackingDto toOrderItemTrackingDto(OrderItem item) {
        OrderItemTrackingDto dto = new OrderItemTrackingDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProductName());
        dto.setProductSku(item.getProductSku());
        dto.setProductImage(item.getProduct().getPrimaryImageUrl());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getPrice());
        dto.setSubtotal(item.getSubtotal());
        dto.setDiscountAmount(item.getDiscountAmount());
        dto.setFinalPrice(item.getFinalPrice());
        return dto;
    }
    
    private List<OrderStatusHistoryDto> createStatusHistory(Order order) {
        List<OrderStatusHistoryDto> history = new ArrayList<>();
        
        // Order created
        history.add(new OrderStatusHistoryDto(
            "PENDING",
            "Đơn hàng đã được tạo",
            "Đơn hàng đã được tạo và đang chờ xử lý",
            order.getCreatedAt(),
            "System"
        ));
        
        // Payment processed
        if (order.getPaidAt() != null) {
            history.add(new OrderStatusHistoryDto(
                "PAID",
                "Đã thanh toán",
                "Đơn hàng đã được thanh toán thành công",
                order.getPaidAt(),
                "System"
            ));
        }
        
        // Status updates
        if (order.getStatus() == Order.OrderStatus.PROCESSING) {
            history.add(new OrderStatusHistoryDto(
                "PROCESSING",
                "Đang xử lý",
                "Đơn hàng đang được xử lý",
                order.getUpdatedAt(),
                "Admin"
            ));
        } else if (order.getStatus() == Order.OrderStatus.SHIPPED) {
            history.add(new OrderStatusHistoryDto(
                "SHIPPED",
                "Đã gửi hàng",
                "Đơn hàng đã được gửi đi",
                order.getUpdatedAt(),
                "Admin"
            ));
        } else if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            history.add(new OrderStatusHistoryDto(
                "DELIVERED",
                "Đã giao hàng",
                "Đơn hàng đã được giao thành công",
                order.getDeliveredAt(),
                "System"
            ));
        } else if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            history.add(new OrderStatusHistoryDto(
                "CANCELLED",
                "Đã hủy",
                "Đơn hàng đã bị hủy: " + order.getCancellationReason(),
                order.getCancelledAt(),
                "Customer"
            ));
        }
        
        return history;
    }
    
    private String getStatusDescription(Order.OrderStatus status) {
        switch (status) {
            case PENDING:
                return "Đang chờ xử lý";
            case PAID:
                return "Đã thanh toán";
            case PROCESSING:
                return "Đang xử lý";
            case SHIPPED:
                return "Đã gửi hàng";
            case DELIVERED:
                return "Đã giao hàng";
            case CANCELLED:
                return "Đã hủy";
            case REFUNDED:
                return "Đã hoàn tiền";
            default:
                return "Không xác định";
        }
    }
}
