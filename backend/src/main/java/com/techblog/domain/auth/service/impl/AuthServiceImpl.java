package com.techblog.domain.auth.service.impl;

import com.techblog.common.enums.RoleName;
import com.techblog.domain.auth.dto.AuthResponse;
import com.techblog.domain.auth.dto.LoginRequest;
import com.techblog.domain.auth.dto.RegisterRequest;
import com.techblog.domain.auth.service.AuthService;
import com.techblog.domain.user.model.Role;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.model.UserRole;
import com.techblog.domain.user.repository.RoleRepository;
import com.techblog.domain.user.repository.UserRepository;
import com.techblog.domain.user.repository.UserRoleRepository;
import com.techblog.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));

        User user = new User();
        user.setUsername(request.getFullName()); // tạm map fullName -> username
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        // status mặc định đã là ACTIVE trong entity User

        user = userRepository.save(user);

        UserRole userRoleMapping = new UserRole();
        userRoleMapping.setUser(user);
        userRoleMapping.setRole(userRole);
        userRoleRepository.save(userRoleMapping);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Set<String> roles = userRoleRepository.findByUser(user)
                    .stream()
                    .map(userRole -> userRole.getRole().getName().name())
                    .collect(Collectors.toSet());

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getUsername())
                    .roles(roles)
                    .build();

        } catch (LockedException ex) {
            throw new IllegalStateException("User is banned");
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}