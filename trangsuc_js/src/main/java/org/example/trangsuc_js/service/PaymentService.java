package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.order.CreateOrderDto;
import org.example.trangsuc_js.dto.order.OrderDto;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentLinkData;

import java.util.List;

public interface PaymentService {
    OrderDto createOrder(CreateOrderDto dto);
    
    CheckoutResponseData createPayment(Long orderId);
    
    CheckoutResponseData createPaymentLink(String productName, String description, 
                                         String returnUrl, String cancelUrl, int price);
    
    PaymentLinkData getPaymentLinkInfo(Long orderCode);
    
    PaymentLinkData cancelPaymentLink(Long orderCode, String reason);
    
    void handlePaymentWebhook(String payosPaymentId, String status);
    
    OrderDto getOrderByOrderNumber(String orderNumber);
    
    List<OrderDto> getUserOrders();
    
    OrderDto updateOrderStatus(Long orderId, String status);
    
    OrderDto confirmPayment(String orderCode, String paymentId);
}