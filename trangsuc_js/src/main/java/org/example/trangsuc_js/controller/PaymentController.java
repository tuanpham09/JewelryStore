package org.example.trangsuc_js.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trangsuc_js.dto.order.CreateOrderDto;
import org.example.trangsuc_js.dto.order.OrderDto;
import org.example.trangsuc_js.service.PaymentService;
import org.example.trangsuc_js.type.CreatePaymentLinkRequestBody;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    
    private final PaymentService paymentService;
    private final PayOS payOS;

    @PostMapping("/create-order")
    public ApiResponse<OrderDto> createOrder(@RequestBody CreateOrderDto dto) {
        log.info("Creating order: {}", dto);
        OrderDto order = paymentService.createOrder(dto);
        return ApiResponse.success("Order created successfully", order);
    }

    @PostMapping("/create-payment/{orderId}")
    public ApiResponse<CheckoutResponseData> createPayment(@PathVariable Long orderId) {
        log.info("Creating payment for order: {}", orderId);
        CheckoutResponseData response = paymentService.createPayment(orderId);
        return ApiResponse.success("Payment created successfully", response);
    }

    @PostMapping("/create-payment-link")
    public ObjectNode createPaymentLink(@RequestBody CreatePaymentLinkRequestBody requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        
        try {
            final String productName = requestBody.getProductName();
            final String description = requestBody.getDescription();
            final String returnUrl = requestBody.getReturnUrl();
            final String cancelUrl = requestBody.getCancelUrl();
            final int price = requestBody.getPrice();
            
            CheckoutResponseData data = paymentService.createPaymentLink(
                productName, description, returnUrl, cancelUrl, price);

            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (Exception e) {
            log.error("Error creating payment link: ", e);
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }
    }

    @GetMapping("/payment-link/{orderCode}")
    public ObjectNode getPaymentLinkInfo(@PathVariable("orderCode") long orderCode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            PaymentLinkData paymentLinkData = paymentService.getPaymentLinkInfo(orderCode);

            response.set("data", objectMapper.valueToTree(paymentLinkData));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error getting payment link info: ", e);
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PutMapping("/payment-link/{orderCode}/cancel")
    public ObjectNode cancelPaymentLink(@PathVariable("orderCode") long orderCode, 
                                      @RequestParam(required = false) String reason) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        
        try {
            PaymentLinkData paymentLinkData = paymentService.cancelPaymentLink(orderCode, reason);
            response.set("data", objectMapper.valueToTree(paymentLinkData));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error canceling payment link: ", e);
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PostMapping("/confirm-webhook")
    public ObjectNode confirmWebhook(@RequestBody Map<String, String> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        
        try {
            String result = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(result));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error confirming webhook: ", e);
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PostMapping("/webhook")
    public ObjectNode handleWebhook(@RequestBody ObjectNode body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);

        try {
            // Init Response
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);

            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            log.info("Webhook data verified: {}", data);
            
            // Xử lý webhook data nếu cần
            paymentService.handlePaymentWebhook(data.getPaymentLinkId(), data.getCode());
            
            return response;
        } catch (Exception e) {
            log.error("Error processing webhook: ", e);
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @GetMapping("/order/{orderNumber}")
    public ApiResponse<OrderDto> getOrder(@PathVariable String orderNumber) {
        OrderDto order = paymentService.getOrderByOrderNumber(orderNumber);
        return ApiResponse.success("Order retrieved successfully", order);
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderDto>> getUserOrders() {
        List<OrderDto> orders = paymentService.getUserOrders();
        return ApiResponse.success("Orders retrieved successfully", orders);
    }

    @PutMapping("/order/{orderId}/status")
    public ApiResponse<OrderDto> updateOrderStatus(@PathVariable Long orderId, 
                                                  @RequestParam String status) {
        log.info("Updating order status: orderId={}, status={}", orderId, status);
        OrderDto order = paymentService.updateOrderStatus(orderId, status);
        return ApiResponse.success("Order status updated successfully", order);
    }
}