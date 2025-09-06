package com.example.pizza_backend.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String productName;
    private String productDetail;
    private String productImg;
    private Integer productPrice;
    private Integer productStock;
    private Integer isActive;
    private LocalDate createdAt;
    private String createdBy;
    private LocalDate updatedAt;
    private String updatedBy;

    @Transient
    public String getProductImgPath(){
        if (productImg == null) return null;

        return "/product-photos/"+ productImg;
    }
}
