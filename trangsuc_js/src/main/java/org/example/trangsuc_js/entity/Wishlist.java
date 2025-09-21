package org.example.trangsuc_js.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "wishlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class Wishlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "is_notified")
    private Boolean isNotified = false; // Đã thông báo khi sản phẩm giảm giá
}
