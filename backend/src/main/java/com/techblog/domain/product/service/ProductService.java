package com.techblog.domain.product.service;

import com.techblog.domain.product.dto.CreateProductRequest;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.dto.ProductStatusUpdateRequest;
import java.util.List;

public interface ProductService {

    List<ProductResponse> getPublicProducts(String categorySlug, String brand, String sort);

    ProductResponse getPublicProductBySlug(String slug);

    ProductResponse createProduct(CreateProductRequest request, String actorEmail);

    ProductResponse updateProduct(Long id, CreateProductRequest request);

    ProductResponse updateProductStatus(Long id, ProductStatusUpdateRequest request);

    void softDeleteProduct(Long id);

    ProductResponse restoreProduct(Long id);
}