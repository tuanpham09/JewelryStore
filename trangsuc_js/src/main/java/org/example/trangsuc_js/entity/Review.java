package org.example.trangsuc_js.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Column(nullable = false)
    @Min(1) @Max(5)
    private Integer rating; // 1-5
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Bổ sung các trường cần thiết
    @Column(name = "is_verified_purchase")
    private Boolean isVerifiedPurchase = false;
    
    @Column(name = "helpful_count")
    private Integer helpfulCount = 0;
    
    @Column(name = "is_approved")
    private Boolean isApproved = true; // Admin có thể approve/reject reviews
    
    @Column(name = "admin_comment")
    private String adminComment; // Admin có thể comment về review
}
