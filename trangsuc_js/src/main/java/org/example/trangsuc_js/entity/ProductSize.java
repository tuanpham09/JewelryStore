package org.example.trangsuc_js.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "product_sizes")
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Boolean isActive = true;

    // Constructors
    public ProductSize() {}

    public ProductSize(Product product, String size, Integer stock) {
        this.product = product;
        this.size = size;
        this.stock = stock;
    }
}
