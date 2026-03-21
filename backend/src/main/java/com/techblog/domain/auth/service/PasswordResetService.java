package com.techblog.domain.auth.service;

public interface PasswordResetService {

    void createResetToken(String email);

    void validateResetToken(String token);

    void resetPassword(String token, String newPassword);
}
