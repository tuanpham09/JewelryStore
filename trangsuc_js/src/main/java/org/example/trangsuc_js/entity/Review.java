package org.example.trangsuc_js.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name="reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","product_id"}))
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="user_id")
    private User user;

    @ManyToOne @JoinColumn(name="product_id")
    private Product product;

    private int rating; // 1-5
    private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();
}
