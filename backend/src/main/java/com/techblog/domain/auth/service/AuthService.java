package com.techblog.domain.auth.service;

import com.techblog.domain.auth.dto.AuthResponse;
import com.techblog.domain.auth.dto.LoginRequest;
import com.techblog.domain.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void verifyEmail(String token);
    void forgotPassword(String email);
    void validateResetToken(String token);
    void resetPassword(String token, String newPassword);
}
