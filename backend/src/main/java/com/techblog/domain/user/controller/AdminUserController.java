package com.techblog.domain.user.controller;

import com.techblog.common.response.ApiResponse;
import com.techblog.domain.user.dto.AdminUserListResponse;
import com.techblog.domain.user.dto.AdminUserSummaryResponse;
import com.techblog.domain.user.dto.AssignRolesRequest;
import com.techblog.domain.user.dto.BanUserRequest;
import com.techblog.domain.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminUserListResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Users retrieved successfully",
                adminUserService.getUsers(page, size, keyword, status)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminUserSummaryResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "User retrieved successfully",
                adminUserService.getUserById(id)
        ));
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<Void>> assignRoles(
            @PathVariable Long id,
            @Valid @RequestBody AssignRolesRequest request,
            Authentication authentication) {
        adminUserService.assignRoles(id, request, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "User roles updated successfully", null));
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<ApiResponse<Void>> banUser(
            @PathVariable Long id,
            @Valid @RequestBody BanUserRequest request,
            Authentication authentication) {
        adminUserService.banUser(id, request, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "User banned successfully", null));
    }

    @PatchMapping("/{id}/unban")
    public ResponseEntity<ApiResponse<Void>> unbanUser(@PathVariable Long id, Authentication authentication) {
        adminUserService.unbanUser(id, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "User unbanned successfully", null));
    }
}
