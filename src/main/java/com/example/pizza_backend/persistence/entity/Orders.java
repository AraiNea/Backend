package com.example.pizza_backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addr_id")
    private Address address;

    private Integer status;
    private Integer subtotal;
    private Integer deliveryFee;
    private Integer grandTotal;
    private LocalDate createdAt;
    private LocalDate fulfilledAt;
    private String fulfilledBy;
}
