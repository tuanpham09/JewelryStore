package org.example.trangsuc_js.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayosPaymentResponse {
    private Integer code;
    private String message;
    @JsonProperty("desc")
    private String desc;
    private Data data;
    
    @lombok.Data
    public static class Data {
        private String bin;
        private String accountNumber;
        private String accountName;
        private Long amount;
        private String description;
        private Integer orderCode;
        private String currency;
        private String paymentLinkId;
        private String status;
        private String checkoutUrl;
        private String qrCode;
    }
}
