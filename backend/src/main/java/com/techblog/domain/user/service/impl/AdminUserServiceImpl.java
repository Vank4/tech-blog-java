package com.techblog.domain.user.service.impl;

import com.techblog.common.enums.RoleName;
import com.techblog.common.enums.UserStatus;
import com.techblog.domain.notification.service.NotificationService;
import com.techblog.domain.user.dto.AdminUserListResponse;
import com.techblog.domain.user.dto.AdminUserSummaryResponse;
import com.techblog.domain.user.dto.AssignRolesRequest;
import com.techblog.domain.user.dto.BanUserRequest;
import com.techblog.domain.user.model.Role;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.model.UserRole;
import com.techblog.domain.user.repository.RoleRepository;
import com.techblog.domain.user.repository.UserRepository;
import com.techblog.domain.user.repository.UserRoleRepository;
import com.techblog.domain.user.service.AdminUserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public AdminUserListResponse getUsers(int page, int size, String keyword, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String normalizedKeyword = keyword != null && !keyword.isBlank() ? keyword.trim() : null;
        UserStatus normalizedStatus = status != null && !status.isBlank()
                ? UserStatus.valueOf(status.trim().toUpperCase())
                : null;
        Page<User> users = userRepository.searchUsers(normalizedKeyword, normalizedStatus, pageable);

        return AdminUserListResponse.builder()
                .content(users.getContent().stream().map(this::mapToSummary).toList())
                .page(users.getNumber())
                .size(users.getSize())
                .totalElements(users.getTotalElements())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserSummaryResponse getUserById(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToSummary(user);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, AssignRolesRequest request, String adminEmail) {
        User admin = findUserByEmail(adminEmail);
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        validateAdminAction(admin, user, "update roles");

        Set<RoleName> targetRoles = new HashSet<>(request.getRoles());
        List<Role> roles = targetRoles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName.name())))
                .toList();

        user.getUserRoles().clear();
        userRoleRepository.deleteByUser(user);
        userRoleRepository.flush();
        roles.forEach(role -> {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        });

        notificationService.createNotification(
                user,
                "ROLE_UPDATED",
                "Your account roles were updated",
                "Your roles are now: " + targetRoles.stream().map(Enum::name).sorted().collect(Collectors.joining(", ")),
                "/profile"
        );
    }

    @Override
    @Transactional
    public void banUser(Long userId, BanUserRequest request, String adminEmail) {
        User admin = findUserByEmail(adminEmail);
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        validateAdminAction(admin, user, "ban");

        user.setStatus(UserStatus.BANNED);
        userRepository.save(user);

        notificationService.createNotification(
                user,
                "ACCOUNT_BANNED",
                "Your account was banned",
                "Reason: " + request.getReason(),
                null
        );
    }

    @Override
    @Transactional
    public void unbanUser(Long userId, String adminEmail) {
        User admin = findUserByEmail(adminEmail);
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        validateAdminAction(admin, user, "unban");

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        notificationService.createNotification(
                user,
                "ACCOUNT_UNBANNED",
                "Your account was reactivated",
                "Your account can now be used normally.",
                "/login"
        );
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void validateAdminAction(User admin, User targetUser, String action) {
        if (admin.getId().equals(targetUser.getId())) {
            throw new IllegalStateException("You cannot " + action + " your own account");
        }
    }

    private AdminUserSummaryResponse mapToSummary(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return AdminUserSummaryResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getDisplayName())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .emailVerified(user.isEmailVerified())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
