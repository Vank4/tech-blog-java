package com.techblog.domain.product.service;

import com.techblog.domain.product.dto.CreateProductRequest;
import com.techblog.domain.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(CreateProductRequest request);
    List<ProductResponse> findAll();
    ProductResponse findById(Long id);
}