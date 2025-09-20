package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.payment.PaymentMethodDto;
import org.example.trangsuc_js.dto.payment.PaymentRequestDto;
import org.example.trangsuc_js.dto.payment.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    List<PaymentMethodDto> getAvailablePaymentMethods();
    PaymentResponseDto processPayment(PaymentRequestDto request);
    PaymentResponseDto getPaymentStatus(String paymentId);
    PaymentResponseDto cancelPayment(String paymentId);
    PaymentResponseDto refundPayment(String paymentId, String reason);
}
