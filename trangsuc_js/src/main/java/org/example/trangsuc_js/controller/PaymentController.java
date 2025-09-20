package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.payment.PaymentMethodDto;
import org.example.trangsuc_js.dto.payment.PaymentRequestDto;
import org.example.trangsuc_js.dto.payment.PaymentResponseDto;
import org.example.trangsuc_js.service.PaymentService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @GetMapping("/methods")
    public ResponseEntity<ApiResponse<List<PaymentMethodDto>>> getPaymentMethods() {
        List<PaymentMethodDto> methods = paymentService.getAvailablePaymentMethods();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Payment methods retrieved successfully",
            methods
        ));
    }
    
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processPayment(
            @Valid @RequestBody PaymentRequestDto request) {
        PaymentResponseDto response = paymentService.processPayment(request);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Payment processed successfully",
            response
        ));
    }
    
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentStatus(
            @PathVariable String paymentId) {
        PaymentResponseDto response = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Payment status retrieved successfully",
            response
        ));
    }
    
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> cancelPayment(
            @PathVariable String paymentId) {
        PaymentResponseDto response = paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Payment cancelled successfully",
            response
        ));
    }
    
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> refundPayment(
            @PathVariable String paymentId,
            @RequestParam String reason) {
        PaymentResponseDto response = paymentService.refundPayment(paymentId, reason);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Refund processed successfully",
            response
        ));
    }
}
