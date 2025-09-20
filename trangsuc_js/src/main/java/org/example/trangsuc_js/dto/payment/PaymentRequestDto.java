package org.example.trangsuc_js.dto.payment;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    @NotNull(message = "Order ID không được để trống")
    private Long orderId;
    
    @NotBlank(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;
    
    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private BigDecimal amount;
    
    private String description;
    private String returnUrl;
    private String cancelUrl;
    
    // Credit card info (if applicable)
    private String cardNumber;
    private String cardHolderName;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    
    // Bank transfer info
    private String bankCode;
    private String accountNumber;
    
    // VNPay specific
    private String vnpOrderInfo;
    private String vnpReturnUrl;
    
    // Momo specific
    private String momoOrderInfo;
    private String momoReturnUrl;
}
