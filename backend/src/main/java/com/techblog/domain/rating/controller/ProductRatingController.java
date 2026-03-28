package com.techblog.domain.rating.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.rating.dto.ProductRatingRequest;
import com.techblog.domain.rating.dto.ProductRatingSummaryResponse;
import com.techblog.domain.rating.service.ProductRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductRatingController {

    private final ProductRatingService productRatingService;

    @PostMapping("/{productId}/rating")
    public ResponseEntity<ApiResponse<ProductRatingSummaryResponse>> upsertRating(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRatingRequest request,
            Authentication authentication) {
        ProductRatingSummaryResponse response = productRatingService.upsertRating(
                productId,
                request.getRating(),
                authentication.getName());

        return ResponseEntity.ok(new ApiResponse<>(true, "Product rating saved successfully", response));
    }

    @GetMapping("/{productId}/rating-summary")
    public ResponseEntity<ApiResponse<ProductRatingSummaryResponse>> getRatingSummary(
            @PathVariable Long productId,
            Authentication authentication) {
        ProductRatingSummaryResponse response = productRatingService.getRatingSummary(
                productId,
                authentication != null ? authentication.getName() : null);

        return ResponseEntity.ok(new ApiResponse<>(true, "Product rating summary retrieved successfully", response));
    }
}