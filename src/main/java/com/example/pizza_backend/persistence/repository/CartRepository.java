package com.example.pizza_backend.persistence.repository;


import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByProfileProfileId(Long profileId);

}
