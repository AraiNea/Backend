package com.example.pizza_backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class RecommendedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String recommendedImg;
    private String recProductPath;
    private Integer priority;
}
