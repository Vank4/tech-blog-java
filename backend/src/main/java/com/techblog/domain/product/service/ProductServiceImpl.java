package com.techblog.domain.product.service;

import com.techblog.domain.category.model.Category;
import com.techblog.domain.category.repository.CategoryRepository;
import com.techblog.domain.product.dto.CreateProductRequest;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.model.Product;
import com.techblog.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setBrand(request.getBrand());
        product.setModel(request.getModel());
        product.setShortDesc(request.getShortDesc());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setThumbnailUrl(request.getThumbnailUrl());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        product = productRepository.save(product);
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setBrand(product.getBrand());
        response.setModel(product.getModel());
        response.setShortDesc(product.getShortDesc());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setThumbnailUrl(product.getThumbnailUrl());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
        }

        return response;
    }
}