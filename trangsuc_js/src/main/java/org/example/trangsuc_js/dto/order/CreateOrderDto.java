package org.example.trangsuc_js.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String notes;
    private List<OrderItemDto> items;
    
    @Data
    public static class OrderItemDto {
        private Long productId;
        private Integer quantity;
        private String sizeValue;
        private String colorValue;
    }
}
