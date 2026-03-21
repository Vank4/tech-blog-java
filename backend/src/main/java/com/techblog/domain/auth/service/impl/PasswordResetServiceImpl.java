package com.techblog.domain.auth.service.impl;

import com.techblog.config.AppProperties;
import com.techblog.domain.auth.model.PasswordResetToken;
import com.techblog.domain.auth.repository.PasswordResetTokenRepository;
import com.techblog.domain.auth.service.PasswordResetService;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordEmailSender resetPasswordEmailSender;
    private final AppProperties appProperties;

    @Override
    @Transactional
    public void createResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PasswordResetToken token = passwordResetTokenRepository.findByUser(user)
                .orElseGet(PasswordResetToken::new);

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(appProperties.getPasswordReset().getExpirationMinutes()));
        token.setUsedAt(null);

        passwordResetTokenRepository.save(token);
        resetPasswordEmailSender.sendResetPasswordEmail(user, buildResetUrl(token.getToken()));
    }

    @Override
    @Transactional(readOnly = true)
    public void validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Reset token is invalid"));
        ensureTokenUsable(resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Reset token is invalid"));
        ensureTokenUsable(resetToken);

        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(resetToken);
    }

    private void ensureTokenUsable(PasswordResetToken resetToken) {
        if (resetToken.getUsedAt() != null) {
            throw new IllegalArgumentException("Reset token has already been used");
        }
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }
    }

    private String buildResetUrl(String token) {
        String baseUrl = appProperties.getPasswordReset().getBaseUrl();
        String separator = baseUrl.contains("?") ? "&" : "?";
        return baseUrl + separator + "token=" + token;
    }
}
