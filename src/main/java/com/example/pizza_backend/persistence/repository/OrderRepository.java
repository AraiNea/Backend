package com.example.pizza_backend.persistence.repository;

import com.example.pizza_backend.persistence.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> getOrdersByProfileProfileId(Long profileId);
}
