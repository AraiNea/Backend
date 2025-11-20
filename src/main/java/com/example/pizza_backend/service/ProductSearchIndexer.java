package com.example.pizza_backend.service;

import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.entity.ProductDocument;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class ProductSearchIndexer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final ElasticsearchOperations operations;

    @Override
    @Transactional(readOnly = true)
    public void run(ApplicationArguments args) throws Exception {

        List<Product> products = productRepository.findAll();
        System.out.println(products.get(0).getCategory().getCategoryName());

        List<ProductDocument> docs = products.stream()
                .map(p -> ProductDocument.builder()
                        .productId(p.getProductId())
                        .categoryId(p.getCategory().getCategoryId())
                        .categoryName(p.getCategory().getCategoryName())
                        .productName(p.getProductName())
                        .productDetail(p.getProductDetail())
                        .productImg(p.getProductImg())
                        .productPrice(p.getProductPrice())
                        .productStock(p.getProductStock())
                        .isActive(p.getIsActive())
                        .productImgPath(p.getProductImgPath())
                        .build()
                ).toList();

        docs.forEach(doc -> {
            System.out.println("Saving => " + doc);
            operations.save(doc);
        });


        System.out.println("✔ Indexed " + docs.size() + " products");
    }
}


