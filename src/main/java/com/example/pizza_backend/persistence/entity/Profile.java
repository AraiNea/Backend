package com.example.pizza_backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String profileName;
    private String profileSname;
    private Integer profileType;

    // อาจ null (ใช้เมื่อ type = 1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addr_id")
    private Address address;

    private String username;
    private String password;
    private LocalDate createdAt;
}
