package com.example.pizza_backend.config;

import com.example.pizza_backend.persistence.entity.ProductDocument;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

@Configuration
public class ElasticsearchInitializer {

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchInitializer(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void init() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(ProductDocument.class);

        if (!indexOps.exists()) {
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            System.out.println("✅ Created Elasticsearch index 'product' automatically.");
        } else {
            System.out.println("ℹ️ Elasticsearch index 'product' already exists.");
        }
    }
}
