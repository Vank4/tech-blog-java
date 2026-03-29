package com.techblog.domain.interaction.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.interaction.dto.CompareResponse;
import com.techblog.domain.interaction.service.CompareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCompareController {

    private final CompareService compareService;

    @PostMapping("/{productId}/compare")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CompareResponse>> addToCompare(
            @PathVariable Long productId,
            Authentication authentication) {
        CompareResponse response = compareService.addItem(authentication.getName(), productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product added to compare successfully", response));
    }

    @DeleteMapping("/{productId}/compare")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> removeFromCompare(
            @PathVariable Long productId,
            Authentication authentication) {
        compareService.removeItem(authentication.getName(), productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product removed from compare successfully", null));
    }
}
