package org.example.trangsuc_js.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne @JoinColumn(name="product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    // Bổ sung các trường cần thiết
    @Column(name = "product_name")
    private String productName; // Lưu tên sản phẩm tại thời điểm mua
    
    @Column(name = "product_sku")
    private String productSku; // Mã SKU sản phẩm
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Helper methods
    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
    
    public BigDecimal getFinalPrice() {
        return getSubtotal().subtract(discountAmount);
    }
}