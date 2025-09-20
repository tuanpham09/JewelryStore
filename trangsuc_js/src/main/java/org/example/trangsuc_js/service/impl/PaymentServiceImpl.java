package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.payment.PaymentMethodDto;
import org.example.trangsuc_js.dto.payment.PaymentRequestDto;
import org.example.trangsuc_js.dto.payment.PaymentResponseDto;
import org.example.trangsuc_js.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodDto> getAvailablePaymentMethods() {
        return Arrays.asList(
            new PaymentMethodDto(
                "COD",
                "Thanh toán khi nhận hàng",
                "Thanh toán bằng tiền mặt khi nhận hàng",
                "cash-icon",
                true,
                false,
                0.0,
                "VND"
            ),
            new PaymentMethodDto(
                "BANK_TRANSFER",
                "Chuyển khoản ngân hàng",
                "Chuyển khoản qua ngân hàng",
                "bank-icon",
                true,
                true,
                0.0,
                "VND"
            ),
            new PaymentMethodDto(
                "CREDIT_CARD",
                "Thẻ tín dụng",
                "Thanh toán bằng thẻ tín dụng Visa/MasterCard",
                "credit-card-icon",
                true,
                true,
                2.5,
                "VND"
            ),
            new PaymentMethodDto(
                "VNPAY",
                "VNPay",
                "Thanh toán qua VNPay",
                "vnpay-icon",
                true,
                true,
                1.5,
                "VND"
            ),
            new PaymentMethodDto(
                "MOMO",
                "Ví MoMo",
                "Thanh toán qua ví điện tử MoMo",
                "momo-icon",
                true,
                true,
                1.0,
                "VND"
            ),
            new PaymentMethodDto(
                "PAYPAL",
                "PayPal",
                "Thanh toán qua PayPal",
                "paypal-icon",
                true,
                true,
                3.0,
                "USD"
            )
        );
    }
    
    @Override
    @Transactional
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        String paymentId = UUID.randomUUID().toString();
        
        // Simulate payment processing based on method
        switch (request.getPaymentMethod().toUpperCase()) {
            case "COD":
                return processCODPayment(paymentId, request);
            case "BANK_TRANSFER":
                return processBankTransferPayment(paymentId, request);
            case "CREDIT_CARD":
                return processCreditCardPayment(paymentId, request);
            case "VNPAY":
                return processVNPayPayment(paymentId, request);
            case "MOMO":
                return processMomoPayment(paymentId, request);
            case "PAYPAL":
                return processPayPalPayment(paymentId, request);
            default:
                return createErrorResponse(paymentId, "UNSUPPORTED_METHOD", "Phương thức thanh toán không được hỗ trợ");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentStatus(String paymentId) {
        // In production, this would query the database or payment gateway
        return new PaymentResponseDto(
            paymentId,
            "SUCCESS",
            "VNPAY",
            BigDecimal.valueOf(1000000),
            "VND",
            "TXN_" + paymentId,
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Payment successful",
            null,
            null
        );
    }
    
    @Override
    @Transactional
    public PaymentResponseDto cancelPayment(String paymentId) {
        // In production, this would cancel the payment with the gateway
        return new PaymentResponseDto(
            paymentId,
            "CANCELLED",
            null,
            null,
            null,
            null,
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Payment cancelled successfully",
            null,
            null
        );
    }
    
    @Override
    @Transactional
    public PaymentResponseDto refundPayment(String paymentId, String reason) {
        // In production, this would process refund with the gateway
        return new PaymentResponseDto(
            paymentId,
            "REFUNDED",
            null,
            null,
            null,
            null,
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Refund processed successfully",
            null,
            null
        );
    }
    
    private PaymentResponseDto processCODPayment(String paymentId, PaymentRequestDto request) {
        return new PaymentResponseDto(
            paymentId,
            "SUCCESS",
            "COD",
            request.getAmount(),
            "VND",
            "COD_" + paymentId,
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Thanh toán khi nhận hàng đã được xác nhận",
            null,
            null
        );
    }
    
    private PaymentResponseDto processBankTransferPayment(String paymentId, PaymentRequestDto request) {
        return new PaymentResponseDto(
            paymentId,
            "PENDING",
            "BANK_TRANSFER",
            request.getAmount(),
            "VND",
            "BANK_" + paymentId,
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Vui lòng chuyển khoản theo thông tin được gửi qua email",
            null,
            null
        );
    }
    
    private PaymentResponseDto processCreditCardPayment(String paymentId, PaymentRequestDto request) {
        // Simulate credit card processing
        if (request.getCardNumber() == null || request.getCardNumber().length() < 16) {
            return createErrorResponse(paymentId, "INVALID_CARD", "Thông tin thẻ không hợp lệ");
        }
        
        return new PaymentResponseDto(
            paymentId,
            "SUCCESS",
            "CREDIT_CARD",
            request.getAmount(),
            "VND",
            "CC_" + paymentId,
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Thanh toán thẻ tín dụng thành công",
            null,
            null
        );
    }
    
    private PaymentResponseDto processVNPayPayment(String paymentId, PaymentRequestDto request) {
        // Simulate VNPay integration
        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=" + 
                           request.getAmount().multiply(BigDecimal.valueOf(100)) + 
                           "&vnp_Command=pay&vnp_CreateDate=" + System.currentTimeMillis() +
                           "&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=" +
                           request.getVnpOrderInfo() + "&vnp_OrderType=other&vnp_ReturnUrl=" +
                           request.getVnpReturnUrl() + "&vnp_TmnCode=YOUR_TMN_CODE&vnp_TxnRef=" + paymentId;
        
        return new PaymentResponseDto(
            paymentId,
            "PENDING",
            "VNPAY",
            request.getAmount(),
            "VND",
            "VNP_" + paymentId,
            paymentUrl,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Vui lòng thanh toán qua VNPay",
            null,
            null
        );
    }
    
    private PaymentResponseDto processMomoPayment(String paymentId, PaymentRequestDto request) {
        // Simulate MoMo integration
        String paymentUrl = "https://test-payment.momo.vn/v2/gateway/pay?partnerCode=YOUR_PARTNER_CODE&" +
                           "accessKey=YOUR_ACCESS_KEY&requestId=" + paymentId + "&amount=" + 
                           request.getAmount().multiply(BigDecimal.valueOf(100)) + "&orderId=" + paymentId +
                           "&orderInfo=" + request.getMomoOrderInfo() + "&returnUrl=" + request.getMomoReturnUrl();
        
        return new PaymentResponseDto(
            paymentId,
            "PENDING",
            "MOMO",
            request.getAmount(),
            "VND",
            "MOMO_" + paymentId,
            paymentUrl,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Vui lòng thanh toán qua MoMo",
            null,
            null
        );
    }
    
    private PaymentResponseDto processPayPalPayment(String paymentId, PaymentRequestDto request) {
        // Simulate PayPal integration
        String paymentUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + paymentId;
        
        return new PaymentResponseDto(
            paymentId,
            "PENDING",
            "PAYPAL",
            request.getAmount(),
            "USD",
            "PP_" + paymentId,
            paymentUrl,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Vui lòng thanh toán qua PayPal",
            null,
            null
        );
    }
    
    private PaymentResponseDto createErrorResponse(String paymentId, String errorCode, String errorMessage) {
        return new PaymentResponseDto(
            paymentId,
            "FAILED",
            null,
            null,
            null,
            null,
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Payment failed",
            errorCode,
            errorMessage
        );
    }
}
