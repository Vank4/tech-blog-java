package com.techblog.domain.interaction.controller;

import com.techblog.common.enums.TargetType;
import com.techblog.common.response.ApiResponse;
import com.techblog.domain.interaction.dto.InteractionStatusResponse;
import com.techblog.domain.interaction.dto.InteractionToggleRequest;
import com.techblog.domain.interaction.service.InteractionService;
import com.techblog.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;

    @PostMapping("/like")
    public ResponseEntity<ApiResponse<InteractionStatusResponse>> toggleLike(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody InteractionToggleRequest request) {
        
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Unauthorized", null));
        }
        
        InteractionStatusResponse latestStatus = interactionService.toggleLike(user.getId(), request.getTargetType(), request.getTargetId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Success", latestStatus));
    }

    @PostMapping("/favorite")
    public ResponseEntity<ApiResponse<InteractionStatusResponse>> toggleFavorite(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody InteractionToggleRequest request) {
        
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Unauthorized", null));
        }

        InteractionStatusResponse latestStatus = interactionService.toggleFavorite(user.getId(), request.getTargetType(), request.getTargetId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Success", latestStatus));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<InteractionStatusResponse>> getInteractionStatus(
            @AuthenticationPrincipal User user,
            @RequestParam TargetType targetType,
            @RequestParam Long targetId) {
        
        Long userId = user != null ? user.getId() : null;
        InteractionStatusResponse status = interactionService.getInteractionStatus(userId, targetType, targetId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Success", status));
    }
}
