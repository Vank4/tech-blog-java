package com.techblog.domain.interaction.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.interaction.dto.AddCompareItemRequest;
import com.techblog.domain.interaction.dto.CompareResponse;
import com.techblog.domain.interaction.service.CompareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compare")
@RequiredArgsConstructor
public class CompareController {

    private final CompareService compareService;

    @GetMapping
    public ResponseEntity<ApiResponse<CompareResponse>> getCompare(Authentication authentication) {
        CompareResponse response = compareService.getCompare(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Compare list retrieved successfully", response));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CompareResponse>> addItem(
            @Valid @RequestBody AddCompareItemRequest request,
            Authentication authentication) {
        CompareResponse response = compareService.addItem(authentication.getName(), request.getProductId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Compare item added successfully", response));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeItem(
            @PathVariable Long productId,
            Authentication authentication) {
        compareService.removeItem(authentication.getName(), productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Compare item removed successfully", null));
    }
}