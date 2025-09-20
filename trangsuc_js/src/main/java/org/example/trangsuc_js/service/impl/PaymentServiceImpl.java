package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trangsuc_js.dto.order.CreateOrderDto;
import org.example.trangsuc_js.dto.order.OrderDto;
import org.example.trangsuc_js.entity.*;
import org.example.trangsuc_js.repository.*;
import org.example.trangsuc_js.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentLinkData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final PayOS payOS;
    
    @Value("${app.base-url:http://localhost:4200}")
    private String appBaseUrl;

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderDto dto) {
        log.info("Creating order: {}", dto);
        
        User user = getCurrentUser();
        
        // Tạo order
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setShippingName(dto.getShippingName());
        order.setShippingPhone(dto.getShippingPhone());
        order.setShippingAddress(dto.getShippingAddress());
        order.setNotes(dto.getNotes());
        order.setStatus(Order.OrderStatus.PENDING);
        
        // Thêm items
        for (CreateOrderDto.OrderItemDto itemDto : dto.getItems()) {
            Product product = productRepo.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.getProductId()));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSizeValue(itemDto.getSizeValue());
            orderItem.setColorValue(itemDto.getColorValue());
            orderItem.calculateSubtotal();
            
            order.addItem(orderItem);
        }
        
        // Tính tổng tiền
        order.calculateTotal();
        
        // Lưu order
        order = orderRepo.save(order);
        
        log.info("Order created: {}", order.getOrderNumber());
        return toOrderDto(order);
    }

    @Override
    @Transactional
    public CheckoutResponseData createPayment(Long orderId) {
        log.info("Creating payment for order: {}", orderId);
        
        Order order = orderRepo.findByIdWithItemsAndPayment(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // Kiểm tra total amount
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid order total amount: {}", order.getTotalAmount());
            throw new RuntimeException("Invalid order total amount");
        }
        
        // Tạo payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setDescription("Thanh toán đơn hàng " + order.getOrderNumber());
        
        try {
            // Sử dụng PayOS SDK
            PaymentData paymentData = createPaymentData(order);
            CheckoutResponseData response = payOS.createPaymentLink(paymentData);
            
            // Lưu thông tin payment
            payment.setPayosPaymentId(response.getPaymentLinkId());
            payment.setPayosPaymentUrl(response.getCheckoutUrl());
            payment.setPayosQrCode(response.getQrCode());
            
            payment = paymentRepo.save(payment);
            order.setPayment(payment);
            orderRepo.save(order);
            
            log.info("Payment created successfully: {}", payment.getPayosPaymentId());
            return response;
            
        } catch (Exception e) {
            log.error("Payment creation failed: ", e);
            throw new RuntimeException("Payment creation failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handlePaymentWebhook(String payosPaymentId, String status) {
        log.info("Handling payment webhook: paymentId={}, status={}", payosPaymentId, status);
        
        Payment payment = paymentRepo.findByPayosPaymentId(payosPaymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + payosPaymentId));
        
        Order order = payment.getOrder();
        
        switch (status) {
            case "PAID":
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                payment.setPaidAt(LocalDateTime.now());
                order.setStatus(Order.OrderStatus.PAID);
                break;
            case "CANCELLED":
                payment.setStatus(Payment.PaymentStatus.CANCELLED);
                order.setStatus(Order.OrderStatus.CANCELLED);
                break;
            case "FAILED":
                payment.setStatus(Payment.PaymentStatus.FAILED);
                order.setStatus(Order.OrderStatus.CANCELLED);
                break;
        }
        
        paymentRepo.save(payment);
        orderRepo.save(order);
        
        log.info("Payment status updated: order={}, status={}", order.getOrderNumber(), status);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepo.findByOrderNumberWithItemsAndPayment(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));
        return toOrderDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders() {
        User user = getCurrentUser();
        List<Order> orders = orderRepo.findByUserOrderByCreatedAtDesc(user);
        return orders.stream().map(this::toOrderDto).collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow();
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private PaymentData createPaymentData(Order order) {
        log.info("Creating payment data for order: {}, totalAmount: {}", order.getOrderNumber(), order.getTotalAmount());
        
        // Tạo orderCode unique (timestamp + random)
        int orderCode = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        
        // Kiểm tra và convert amount to VND (multiply by 100 for cents)
        BigDecimal totalAmount = order.getTotalAmount();
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid total amount: {}", totalAmount);
            throw new RuntimeException("Invalid order total amount: " + totalAmount);
        }
        
        // Convert to VND (multiply by 100 for cents) và đảm bảo là số nguyên dương
        long amountInVND = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();
        if (amountInVND <= 0) {
            log.error("Invalid converted amount: {}", amountInVND);
            throw new RuntimeException("Invalid converted amount: " + amountInVND);
        }
        
        log.info("Converted amount: {} VND -> {} cents", totalAmount, amountInVND);
        
        // Tạo items
        List<ItemData> items = order.getItems().stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getUnitPrice();
                    if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new RuntimeException("Invalid unit price for item: " + item.getProduct().getName());
                    }
                    
                    long priceInVND = unitPrice.multiply(BigDecimal.valueOf(100)).longValue();
                    return ItemData.builder()
                            .name(item.getProduct().getName())
                            .quantity(item.getQuantity())
                            .price((int) priceInVND)
                            .build();
                })
                .collect(Collectors.toList());
        
        return PaymentData.builder()
                .orderCode((long) orderCode)
                .amount((int) amountInVND)
                .description("Thanh toán đơn hàng")
                .items(items)
                .cancelUrl(appBaseUrl + "/checkout/cancel")
                .returnUrl(appBaseUrl + "/checkout/success")
                .build();
    }


    private OrderDto toOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingName(order.getShippingName());
        dto.setShippingPhone(order.getShippingPhone());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        // Map items
        dto.setItems(order.getItems().stream().map(item -> {
            OrderDto.OrderItemDto itemDto = new OrderDto.OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setProductImage(item.getProduct().getPrimaryImageUrl());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setSubtotal(item.getSubtotal());
            itemDto.setSizeValue(item.getSizeValue());
            itemDto.setColorValue(item.getColorValue());
            return itemDto;
        }).collect(Collectors.toList()));
        
        // Map payment
        if (order.getPayment() != null) {
            OrderDto.PaymentDto paymentDto = new OrderDto.PaymentDto();
            paymentDto.setId(order.getPayment().getId());
            paymentDto.setPaymentMethod(order.getPayment().getPaymentMethod());
            paymentDto.setAmount(order.getPayment().getAmount());
            paymentDto.setStatus(order.getPayment().getStatus());
            paymentDto.setTransactionId(order.getPayment().getTransactionId());
            paymentDto.setPayosPaymentId(order.getPayment().getPayosPaymentId());
            paymentDto.setPayosPaymentUrl(order.getPayment().getPayosPaymentUrl());
            paymentDto.setPayosQrCode(order.getPayment().getPayosQrCode());
            paymentDto.setDescription(order.getPayment().getDescription());
            paymentDto.setCreatedAt(order.getPayment().getCreatedAt());
            paymentDto.setPaidAt(order.getPayment().getPaidAt());
            dto.setPayment(paymentDto);
        }
        
        return dto;
    }

    @Override
    @Transactional
    public CheckoutResponseData createPaymentLink(String productName, String description, 
                                                String returnUrl, String cancelUrl, int price) {
        log.info("Creating payment link: productName={}, price={}", productName, price);
        
        try {
            // Gen order code
            String currentTimeString = String.valueOf(System.currentTimeMillis());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            // Convert price to VND (multiply by 100 for cents)
            int amountInVND = price * 100;

            ItemData item = ItemData.builder()
                    .name(productName)
                    .price(amountInVND)
                    .quantity(1)
                    .build();

            // Giới hạn description để tránh lỗi validation
            String shortDescription = description.length() > 25 ? description.substring(0, 25) : description;

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(shortDescription)
                    .amount(amountInVND)
                    .item(item)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            CheckoutResponseData response = payOS.createPaymentLink(paymentData);
            log.info("Payment link created successfully: orderCode={}, amount={}", orderCode, amountInVND);
            
            return response;
            
        } catch (Exception e) {
            log.error("Error creating payment link: ", e);
            throw new RuntimeException("Payment link creation failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentLinkData getPaymentLinkInfo(Long orderCode) {
        log.info("Getting payment link info: orderCode={}", orderCode);
        
        try {
            PaymentLinkData paymentLinkData = payOS.getPaymentLinkInformation(orderCode);
            log.info("Payment link info retrieved successfully: orderCode={}", orderCode);
            return paymentLinkData;
            
        } catch (Exception e) {
            log.error("Error getting payment link info: ", e);
            throw new RuntimeException("Failed to get payment link info: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentLinkData cancelPaymentLink(Long orderCode, String reason) {
        log.info("Cancelling payment link: orderCode={}, reason={}", orderCode, reason);
        
        try {
            PaymentLinkData paymentLinkData = payOS.cancelPaymentLink(orderCode, reason);
            log.info("Payment link cancelled successfully: orderCode={}", orderCode);
            return paymentLinkData;
            
        } catch (Exception e) {
            log.error("Error cancelling payment link: ", e);
            throw new RuntimeException("Failed to cancel payment link: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, String status) {
        log.info("Updating order status: orderId={}, status={}", orderId, status);
        
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        
        try {
            switch (status.toUpperCase()) {
                case "CONFIRMED":
                    order.setStatus(Order.OrderStatus.CONFIRMED);
                    break;
                case "PROCESSING":
                    order.setStatus(Order.OrderStatus.PROCESSING);
                    break;
                case "SHIPPED":
                    order.setStatus(Order.OrderStatus.SHIPPED);
                    break;
                case "DELIVERED":
                    order.setStatus(Order.OrderStatus.DELIVERED);
                    order.setDeliveredAt(LocalDateTime.now());
                    break;
                case "CANCELLED":
                    order.setStatus(Order.OrderStatus.CANCELLED);
                    break;
                default:
                    throw new RuntimeException("Invalid order status: " + status);
            }
            
            order.setUpdatedAt(LocalDateTime.now());
            order = orderRepo.save(order);
            
            log.info("Order status updated successfully: orderId={}, status={}", orderId, status);
            return toOrderDto(order);
            
        } catch (Exception e) {
            log.error("Error updating order status: ", e);
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }
}