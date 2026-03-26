package com.techblog.domain.product.service;

import com.techblog.domain.product.dto.CreateProductRequest;
import com.techblog.domain.product.dto.ProductImageRequest;
import com.techblog.domain.product.dto.ProductImageResponse;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.dto.ProductSpecRequest;
import com.techblog.domain.product.dto.ProductSpecResponse;
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

    ProductImageResponse addImage(Long productId, ProductImageRequest request);

    void removeImage(Long productId, Long imageId);

    ProductImageResponse setMainImage(Long productId, Long imageId);

    ProductSpecResponse addSpec(Long productId, ProductSpecRequest request);

    ProductSpecResponse updateSpec(Long productId, Long specId, ProductSpecRequest request);

    void removeSpec(Long productId, Long specId);
}