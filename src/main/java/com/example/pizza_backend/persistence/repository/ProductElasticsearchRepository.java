package com.example.pizza_backend.persistence.repository;

import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.entity.ProductDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductElasticsearchRepository {
    @Autowired
    private ElasticsearchOperations operations;

    public List<ProductDocument> findByProductNameContaining(String name) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(builder -> builder
                        .multiMatch(multiMatch -> multiMatch
                                .fields("productName", "productDetail") // ระบุฟิลด์ที่ต้องการค้นหา
                                .query(name)  // ค่าที่จะค้นหา
                                .fuzziness("AUTO") // ใช้ fuzzy search
                        )
                ).build();

        return operations.search(query, ProductDocument.class) // execute query
                .stream()
                .map(SearchHit::getContent)  // extract content from each hit
                .toList();
    }
}

