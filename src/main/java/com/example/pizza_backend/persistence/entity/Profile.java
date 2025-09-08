package com.example.pizza_backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String profileName;
    private String profileSname;
    private Integer profileRole;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(mappedBy = "profile")
    private Cart cart;

    private String username;
    private String password;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Orders> orders;
}
