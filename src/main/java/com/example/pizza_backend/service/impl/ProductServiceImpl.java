package com.example.pizza_backend.service.impl;


import com.example.pizza_backend.FileUploadUtil;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.api.mapper.Mapper;
import com.example.pizza_backend.exception.IdNotFoundException;
import com.example.pizza_backend.persistence.entity.Cart;
import com.example.pizza_backend.persistence.entity.Category;
import com.example.pizza_backend.persistence.entity.Product;
import com.example.pizza_backend.persistence.repository.CategoryRepository;
import com.example.pizza_backend.persistence.repository.ProductRepository;
import com.example.pizza_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Mapper mapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              Mapper mapper,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductDto> getAllProducts(ProductSearchReq req){
        List<Product> products = productRepository.searchProducts(
                req.getProductId(),
                req.getProductName(),
                req.getProductStock(),
                req.getProductPrice(),
                req.getCategoryId(),
                req.getIsActive()
        );
        return products.stream()
                .map(p -> mapper.toProductDto(p))
                .toList();
    }

    @Override
    @Transactional
    public String createProduct(ProductInput productInput, MultipartFile imageFile, String username) throws IOException {
        if (productInput.getCategoryId() == null) {
            throw new IllegalArgumentException("The given category Id cannot be null");
        }
        Category category = categoryRepository.findById(productInput.getCategoryId())
                .orElseThrow(() -> new IdNotFoundException("Category Not found"));

        Product product = mapper.toProduct(productInput, username);
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        product.setProductImg(fileName);
        product.setCategory(category);
        productRepository.save(product);

        FileUploadUtil.saveFile("Images/product-photos/",imageFile,fileName);

        return "success";
    }

    @Override
    @Transactional
    public String updateProduct(ProductInput productInput, MultipartFile imageFile, String username) throws IOException {
        if (productInput.getProductId() == null) {
            throw new IllegalArgumentException("The given product Id cannot be null");
        }
        Long productId = productInput.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IdNotFoundException("Product Not found"));

        mapper.updateProductFromInput(productInput, product, username);

        if (productInput.getCategoryId() != null){
            Category category = categoryRepository.findById(productInput.getCategoryId())
                    .orElseThrow(() -> new IdNotFoundException("Category Not found"));
            product.setCategory(category);
        }

        if (imageFile != null && !imageFile.isEmpty()){
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            FileUploadUtil.deleteFile("Images/product-photos/",product.getProductImg());
            product.setProductImg(fileName);
            FileUploadUtil.saveFile("Images/product-photos/",imageFile,fileName);
        }


        productRepository.save(product);


        return "success";
    }

    @Override
    @Transactional
    public String deleteProduct(ProductInput productInput) throws IOException {
        if (productInput.getProductId() == null) {
            throw new IllegalArgumentException("The given product Id cannot be null");
        }
        Long productId = productInput.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IdNotFoundException("Product Not found"));
        String filename = product.getProductImg();

        productRepository.deleteById(productId);
        FileUploadUtil.deleteFile("Images/product-photos/",filename);
        return "success";
    }


}
