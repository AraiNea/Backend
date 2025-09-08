package com.example.pizza_backend.persistence.repository;

import com.example.pizza_backend.persistence.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByCartCartId(Long cartId);
}
