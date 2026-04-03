package com.techblog.domain.user.service.impl;

import com.techblog.domain.user.dto.ChangePasswordRequest;
import com.techblog.domain.user.dto.UpdateProfileRequest;
import com.techblog.domain.user.dto.UserProfileResponse;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import com.techblog.domain.user.repository.UserRoleRepository;
import com.techblog.domain.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(String email) {
        User user = findUserByEmail(email);
        return mapToProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateMyProfile(String email, UpdateProfileRequest request) {
        User user = findUserByEmail(email);

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setDisplayName(request.getFullName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        user = userRepository.save(user);
        return mapToProfileResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findUserByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        Set<String> roles = userRoleRepository.findByUser(user)
                .stream()
                .map(userRole -> userRole.getRole().getName().name())
                .collect(Collectors.toSet());

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .emailVerified(user.isEmailVerified())
                .roles(roles)
                .build();
    }
}
