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

    @ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @Column(name="quantity", nullable = false, columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer quantity = 0;

    @Column(name="unit_price", precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10,2) NOT NULL DEFAULT 0.00")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name="subtotal", precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10,2) NOT NULL DEFAULT 0.00")
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name="size_value")
    private String sizeValue;

    @Column(name="color_value")
    private String colorValue;

    // Additional fields for order tracking
    @Column(name="product_name")
    private String productName;

    @Column(name="product_sku")
    private String productSku;

    @Column(name="price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name="discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name="final_price", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Helper methods
    public BigDecimal getSubtotal() {
        if (unitPrice != null && quantity != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    public void calculateSubtotal() {
        this.subtotal = getSubtotal();
    }

    public BigDecimal getPrice() {
        return this.unitPrice;
    }
}