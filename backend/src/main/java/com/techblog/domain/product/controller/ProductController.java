package com.techblog.domain.product.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.product.dto.ProductResponse;
import com.techblog.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "rating-desc") String sort) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Products retrieved successfully",
                productService.getPublicProducts(categorySlug, brand, sort)
        ));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Product retrieved successfully",
                productService.getPublicProductBySlug(slug)
        ));
    }
}