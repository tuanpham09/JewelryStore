package org.example.trangsuc_js.dto.currency;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private String code;
    private String name;
    private String symbol;
    private String flag;
    private BigDecimal exchangeRate;
    private Boolean isActive;
    private Boolean isDefault;
}
