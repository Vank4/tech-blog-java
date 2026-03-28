package com.techblog.domain.product.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.product.dto.ProductImageRequest;
import com.techblog.domain.product.dto.ProductImageResponse;
import com.techblog.domain.product.dto.ProductSpecRequest;
import com.techblog.domain.product.dto.ProductSpecResponse;
import com.techblog.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductAssetController {

    private final ProductService productService;

    @PostMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<ProductImageResponse>> addImage(
            @PathVariable Long productId,
            @Valid @RequestBody ProductImageRequest request) {
        ProductImageResponse response = productService.addImage(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Product image added successfully", response));
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> removeImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        productService.removeImage(productId, imageId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product image removed successfully", null));
    }

    @PatchMapping("/{productId}/images/{imageId}/main")
    public ResponseEntity<ApiResponse<ProductImageResponse>> setMainImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        ProductImageResponse response = productService.setMainImage(productId, imageId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Main product image updated successfully", response));
    }

    @PostMapping("/{productId}/specs")
    public ResponseEntity<ApiResponse<ProductSpecResponse>> addSpec(
            @PathVariable Long productId,
            @Valid @RequestBody ProductSpecRequest request) {
        ProductSpecResponse response = productService.addSpec(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Product spec added successfully", response));
    }

    @PutMapping("/{productId}/specs/{specId}")
    public ResponseEntity<ApiResponse<ProductSpecResponse>> updateSpec(
            @PathVariable Long productId,
            @PathVariable Long specId,
            @Valid @RequestBody ProductSpecRequest request) {
        ProductSpecResponse response = productService.updateSpec(productId, specId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product spec updated successfully", response));
    }

    @DeleteMapping("/{productId}/specs/{specId}")
    public ResponseEntity<ApiResponse<Void>> removeSpec(
            @PathVariable Long productId,
            @PathVariable Long specId) {
        productService.removeSpec(productId, specId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product spec removed successfully", null));
    }
}