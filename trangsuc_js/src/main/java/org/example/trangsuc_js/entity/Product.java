package org.example.trangsuc_js.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String thumbnail;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
}
