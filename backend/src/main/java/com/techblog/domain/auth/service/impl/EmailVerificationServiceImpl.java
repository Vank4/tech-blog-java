package com.techblog.domain.auth.service.impl;

import com.techblog.config.AppProperties;
import com.techblog.domain.auth.model.EmailVerificationToken;
import com.techblog.domain.auth.repository.EmailVerificationTokenRepository;
import com.techblog.domain.auth.service.EmailVerificationService;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;
    private final VerificationEmailSender verificationEmailSender;
    private final AppProperties appProperties;

    @Override
    @Transactional
    public void createAndSendVerification(User user) {
        EmailVerificationToken token = emailVerificationTokenRepository.findByUser(user)
                .orElseGet(EmailVerificationToken::new);

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(appProperties.getEmailVerification().getExpirationMinutes()));
        token.setVerifiedAt(null);

        emailVerificationTokenRepository.save(token);
        verificationEmailSender.sendVerificationEmail(user, buildVerificationUrl(token.getToken()));
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Verification token is invalid"));

        if (verificationToken.getVerifiedAt() != null || verificationToken.getUser().isEmailVerified()) {
            return;
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            emailVerificationTokenRepository.delete(verificationToken);
            throw new IllegalArgumentException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationToken.setVerifiedAt(LocalDateTime.now());
        emailVerificationTokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public void resendVerification(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isEmailVerified()) {
            throw new IllegalStateException("Email is already verified");
        }

        createAndSendVerification(user);
    }

    private String buildVerificationUrl(String token) {
        String baseUrl = appProperties.getEmailVerification().getBaseUrl();
        String separator = baseUrl.contains("?") ? "&" : "?";
        return baseUrl + separator + "token=" + token;
    }
}
