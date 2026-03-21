package com.techblog.domain.user.service;

import com.techblog.domain.user.dto.AdminUserListResponse;
import com.techblog.domain.user.dto.AdminUserSummaryResponse;
import com.techblog.domain.user.dto.AssignRolesRequest;
import com.techblog.domain.user.dto.BanUserRequest;

public interface AdminUserService {

    AdminUserListResponse getUsers(int page, int size, String keyword, String status);

    AdminUserSummaryResponse getUserById(Long id);

    void assignRoles(Long userId, AssignRolesRequest request, String adminEmail);

    void banUser(Long userId, BanUserRequest request, String adminEmail);

    void unbanUser(Long userId, String adminEmail);
}
