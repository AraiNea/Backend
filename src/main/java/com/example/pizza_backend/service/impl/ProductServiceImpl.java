package com.example.pizza_backend.service.impl;


import com.example.pizza_backend.FileUploadUtil;
import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import com.example.pizza_backend.api.mapper.Mapper;
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
                .map(p -> ProductDto.builder()
                        .productId(p.getProductId())
                        .categoryId(p.getCategory().getCategoryId())
                        .productName(p.getProductName())
                        .productDetail(p.getProductDetail())
                        .productPrice(p.getProductPrice())
                        .productStock(p.getProductStock())
                        .productImgPath(p.getProductImgPath())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public String createProduct(ProductInput productInput, MultipartFile imageFile, String username) throws IOException {
        Optional<Category> category = categoryRepository.findById(productInput.getCategoryId());
        if (category.isEmpty()){
            return "not found category";
        }
        Product product = mapper.toProduct(productInput, username);
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        product.setProductImg(fileName);
        product.setCategory(category.get());
        productRepository.save(product);

        FileUploadUtil.saveFile("Images/product-photos/",imageFile,fileName);

        return "success";
    }

    @Override
    @Transactional
    public String updateProduct(ProductInput productInput, MultipartFile imageFile, String username) throws IOException {
        Long productId = productInput.getProductId();
        Optional<Product> product_old = productRepository.findById(productId);
        if (product_old.isEmpty()){
            return "not found old product";
        }
        Product product = product_old.get();

        mapper.updateProductFromInput(productInput, product, username);

        if (productInput.getCategoryId() != null){
            Optional<Category> category = categoryRepository.findById(productInput.getCategoryId());
            if (category.isEmpty()){
                return "not found category";
            }
            product.setCategory(category.get());
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
        Long productId = productInput.getProductId();
        String filename = productRepository.findById(productId).get().getProductImg();

        productRepository.deleteById(productId);
        FileUploadUtil.deleteFile("Images/product-photos/",filename);
        return "success";
    }


}
