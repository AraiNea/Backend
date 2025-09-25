package com.example.pizza_backend.service;

import com.example.pizza_backend.api.dto.ProductDto;
import com.example.pizza_backend.api.dto.input.ProductInput;
import com.example.pizza_backend.api.dto.search.ProductSearchReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts(ProductSearchReq productSearchReq);
    String createProduct(ProductInput productInput, MultipartFile imageFile, String username) throws IOException;
    String updateProduct(ProductInput productInput, MultipartFile imageFile, String username) throws IOException;
}
