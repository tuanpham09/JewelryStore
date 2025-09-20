package org.example.trangsuc_js.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private String paymentId;
    private String status; // PENDING, SUCCESS, FAILED, CANCELLED
    private String paymentMethod;
    private BigDecimal amount;
    private String currency;
    private String transactionId;
    private String paymentUrl; // For online payments
    private String qrCode; // For QR code payments
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
    private String errorCode;
    private String errorMessage;
}
