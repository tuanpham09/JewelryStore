package org.example.trangsuc_js.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

    private String status; // PENDING, PAID, SHIPPED, DELIVERED, CANCELED
    private BigDecimal total;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
    private List<OrderItem> items;
}

