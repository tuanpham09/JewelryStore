package org.example.trangsuc_js.dto.cart;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer itemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemDto> items;
}