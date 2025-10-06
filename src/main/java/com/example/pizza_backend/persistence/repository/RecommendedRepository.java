package com.example.pizza_backend.persistence.repository;


import com.example.pizza_backend.persistence.entity.RecommendedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedRepository extends JpaRepository<RecommendedProduct, Long> {
}
