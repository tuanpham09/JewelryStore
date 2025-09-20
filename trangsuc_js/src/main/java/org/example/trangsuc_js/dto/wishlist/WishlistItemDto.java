package org.example.trangsuc_js.dto.wishlist;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImage;
    private BigDecimal currentPrice;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private Boolean isOnSale;
    private BigDecimal discountPercentage;
    private Boolean isInStock;
    private Integer stock;
    private LocalDateTime addedAt;
    private Boolean hasPriceDrop;
    private BigDecimal priceDropAmount;
}
