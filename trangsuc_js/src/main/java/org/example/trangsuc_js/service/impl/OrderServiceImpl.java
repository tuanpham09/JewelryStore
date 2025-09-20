package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.order.CheckoutDto;
import org.example.trangsuc_js.dto.order.OrderDto;
import org.example.trangsuc_js.dto.order.OrderItemDto;
import org.example.trangsuc_js.entity.*;
import org.example.trangsuc_js.repository.*;
import org.example.trangsuc_js.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;   // 👈 cần có file này
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow();
    }

    @Override
    public OrderDto checkout(CheckoutDto dto) {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        // Lấy cart items đúng user
        List<CartItem> cartItems = cartItemRepo.findAll()
                .stream().filter(i -> i.getCart().equals(cart)).toList();

        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotal(BigDecimal.ZERO);
        order = orderRepo.save(order);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            orderItemRepo.save(oi);
            total = total.add(oi.getPrice());
        }

        order.setTotal(total);
        orderRepo.save(order);

        // Xóa hết item trong giỏ
        cartItemRepo.deleteAll(cartItems);

        return toDto(order);
    }

    @Override
    public List<OrderDto> myOrders() {
        User user = getCurrentUser();
        List<Order> orders = orderRepo.findByUser(user);
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    private OrderDto toDto(Order o) {
        List<OrderItemDto> items = o.getItems().stream()
                .map(oi -> {
                    OrderItemDto dto = new OrderItemDto();
                    dto.setProductId(oi.getProduct().getId());
                    dto.setProductName(oi.getProduct().getName());
                    dto.setQuantity(oi.getQuantity());
                    dto.setPrice(oi.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        OrderDto orderDto = new OrderDto();
        orderDto.setId(o.getId());
        orderDto.setStatus(o.getStatus().name());
        orderDto.setTotal(o.getTotal());
        orderDto.setCreatedAt(o.getCreatedAt());
        orderDto.setItems(items);
        return orderDto;
    }
}
