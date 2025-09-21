package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProductDto {
    private Long productId;
    private String productName;
    private String productThumbnail;
    private Long totalSold;
    private BigDecimal totalRevenue;
}
