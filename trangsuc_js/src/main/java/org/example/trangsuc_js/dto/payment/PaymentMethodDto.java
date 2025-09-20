package org.example.trangsuc_js.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {
    private String code;
    private String name;
    private String description;
    private String icon;
    private Boolean isActive;
    private Boolean isOnline;
    private Double fee;
    private String currency;
}
