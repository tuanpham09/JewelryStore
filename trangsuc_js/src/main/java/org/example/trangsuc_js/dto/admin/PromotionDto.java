package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDto {
    private Long id;
    private String name;
    private String description;
    private String type; // PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING
    private BigDecimal value;
    private BigDecimal minOrderAmount;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private Integer usageLimit;
    private Integer usedCount;
    private String applicableProducts; // ALL, CATEGORY, SPECIFIC
    private String applicableCategories;
    private String applicableProductIds;
}
