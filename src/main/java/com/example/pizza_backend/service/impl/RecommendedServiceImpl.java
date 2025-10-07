package com.example.pizza_backend.service.impl;

import com.example.pizza_backend.FileUploadUtil;
import com.example.pizza_backend.api.dto.RecommendedProductDto;
import com.example.pizza_backend.api.dto.input.RecommendedInput;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Category;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.entity.RecommendedProduct;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import com.example.pizza_backend.persistence.repository.RecommendedRepository;
import com.example.pizza_backend.service.RecommendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class RecommendedServiceImpl implements RecommendedService {

    private final RecommendedRepository recommendedRepository;
    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Autowired
    public RecommendedServiceImpl(RecommendedRepository recommendedRepository, ProductRepository productRepository, Mapper mapper) {
        this.recommendedRepository = recommendedRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }
    @Override
    public List<RecommendedProductDto> getAllRecommendedProducts() {
        List<RecommendedProduct> recommendedProducts = recommendedRepository.findAll();
        return recommendedProducts.stream()
                .map(r -> mapper.toRecommendedProductDto(r))
                .toList();
    }

    @Override
    @Transactional
    public String createRecommended(RecommendedInput recommendedInput) throws IOException {
        if (recommendedInput.getProductId() == null) {
            throw new IllegalArgumentException("The given product Id cannot be null");
        }
        Product product = productRepository.findById(recommendedInput.getProductId())
                .orElseThrow(() -> new IdNotFoundException("Product Not found"));
        RecommendedProduct recommendedProduct = mapper.toRecommendedProduct(recommendedInput);
        String fileName = product.getProductImg();

        Path sourcePath = Paths.get("Images/product-photos/" + fileName);
        Path targetPath = Paths.get("Images/recommended-photos/" + fileName);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        recommendedProduct.setProduct(product);
        recommendedProduct.setRecommendedImg(fileName);
        recommendedRepository.save(recommendedProduct);
        return "success";
    }


    @Override
    @Transactional
    public String deleteRecommended(RecommendedInput recommendedInput) throws IOException {
        if (recommendedInput.getRecommendedId() == null) {
            throw new IllegalArgumentException("The given recommended Id cannot be null");
        }
        Long recommendId = recommendedInput.getRecommendedId();
        RecommendedProduct recommendedProduct = recommendedRepository.findById(recommendId)
                .orElseThrow(() -> new IdNotFoundException("Product Not found"));
        String filename = recommendedProduct.getRecommendedImg();

        recommendedRepository.deleteById(recommendId);
        FileUploadUtil.deleteFile("Images/recommended-photos/",filename);
        return "success";
    }
}
