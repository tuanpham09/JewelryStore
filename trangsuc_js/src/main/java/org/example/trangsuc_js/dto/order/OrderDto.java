package org.example.trangsuc_js.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}
