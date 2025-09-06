package com.example.pizza_backend.persistence.repository;


import com.example.pizza_backend.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE 1=1 " +
            "AND (:productId IS NULL OR p.productId = :productId)" +
            "AND (:productName IS NULL OR p.productName LIKE %:productName%) " +
            "AND (:productPrice IS NULL OR p.productPrice = :productPrice) " +
            "AND (:productStock IS NULL OR p.productStock = :productStock) " +
            "AND (:isActive IS NULL OR p.isActive = :isActive)" +
            "AND (:categoryId IS NULL OR p.category.categoryId = :categoryId)" +
            "order by p.productId")
    List<Product> findAll(
            @Param("productId") Long  productId,
            @Param("productName") String productName,
            @Param("productPrice") String productPrice,
            @Param("productStock") String productStock,
            @Param("categoryId" ) Long categoryId,
            @Param("isActive") String isActive
    );


}
