package com.example.pizza_backend.persistence.repository;


import com.example.pizza_backend.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT c FROM Category c ORDER BY c.categoryPriority")
    List<Category> findAllCategory();
}
