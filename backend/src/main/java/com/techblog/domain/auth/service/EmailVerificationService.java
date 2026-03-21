package com.techblog.domain.auth.service;

import com.techblog.domain.user.model.User;

public interface EmailVerificationService {

    void createAndSendVerification(User user);

    void verifyEmail(String token);

    void resendVerification(String email);
}
