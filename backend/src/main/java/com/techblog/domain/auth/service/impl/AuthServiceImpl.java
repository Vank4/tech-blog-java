package com.techblog.domain.auth.service.impl;

import com.techblog.common.enums.RoleName;
import com.techblog.domain.auth.dto.AuthResponse;
import com.techblog.domain.auth.dto.LoginRequest;
import com.techblog.domain.auth.dto.RegisterRequest;
import com.techblog.domain.auth.service.AuthService;
import com.techblog.domain.auth.service.EmailVerificationService;
import com.techblog.domain.auth.service.PasswordResetService;
import com.techblog.domain.user.model.Role;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.model.UserRole;
import com.techblog.domain.user.repository.RoleRepository;
import com.techblog.domain.user.repository.UserRepository;
import com.techblog.domain.user.repository.UserRoleRepository;
import com.techblog.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email này đã được sử dụng");
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy role mặc định USER"));

        User user = new User();
        user.setUsername(generateUniqueUsername(request.getFullName(), request.getEmail()));
        user.setDisplayName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(false);

        user = userRepository.save(user);

        UserRole userRoleMapping = new UserRole();
        userRoleMapping.setUser(user);
        userRoleMapping.setRole(userRole);
        userRoleRepository.save(userRoleMapping);

        emailVerificationService.createAndSendVerification(user);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Email hoặc mật khẩu không đúng"));

            if (!user.isEmailVerified()) {
                throw new IllegalStateException("Email của bạn chưa được xác thực. Vui lòng kiểm tra hộp thư và xác thực tài khoản trước khi đăng nhập.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            String token = jwtTokenProvider.generateToken(authentication);

            Set<String> roles = userRoleRepository.findByUser(user)
                    .stream()
                    .map(userRole -> userRole.getRole().getName().name())
                    .collect(Collectors.toSet());

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getDisplayName())
                    .roles(roles)
                    .build();

        } catch (LockedException ex) {
            throw new IllegalStateException("Tài khoản của bạn đã bị khóa");
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không đúng");
        }
    }

    @Override
    public void verifyEmail(String token) {
        emailVerificationService.verifyEmail(token);
    }

    @Override
    public void forgotPassword(String email) {
        passwordResetService.createResetToken(email);
    }

    @Override
    public void validateResetToken(String token) {
        passwordResetService.validateResetToken(token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
    }

    private String generateUniqueUsername(String fullName, String email) {
        String baseUsername = fullName == null ? "" : fullName.trim().toLowerCase()
                .replaceAll("[^a-z0-9]+", ".");
        baseUsername = baseUsername.replaceAll("(^\\.)|(\\.$)", "");

        if (baseUsername.isBlank()) {
            baseUsername = email.substring(0, email.indexOf('@')).toLowerCase().replaceAll("[^a-z0-9]+", ".");
        }
        if (baseUsername.isBlank()) {
            baseUsername = "user";
        }

        String candidate = baseUsername;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = baseUsername + suffix++;
        }
        return candidate;
    }
}
