package org.example.trangsuc_js.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="carts")
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", unique = true)
    private User user;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    
    // Bổ sung các trường cần thiết
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "item_count")
    private Integer itemCount = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Helper methods
    public void calculateTotal() {
        if (items == null || items.isEmpty()) {
            this.totalAmount = BigDecimal.ZERO;
            this.itemCount = 0;
        } else {
            this.totalAmount = items.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            this.itemCount = items.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    // Add item to cart
    public void addItem(CartItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        calculateTotal();
    }
    
    // Remove item from cart
    public void removeItem(CartItem item) {
        if (items != null) {
            items.remove(item);
            calculateTotal();
        }
    }
}
