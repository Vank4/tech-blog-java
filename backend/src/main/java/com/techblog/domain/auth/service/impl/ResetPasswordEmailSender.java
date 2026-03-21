package com.techblog.domain.auth.service.impl;

import com.techblog.config.AppProperties;
import com.techblog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordEmailSender {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final AppProperties appProperties;

    public void sendResetPasswordEmail(User user, String resetUrl) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null || !StringUtils.hasText(appProperties.getMail().getFrom())) {
            log.info("Mail sender is not configured. Reset password link for {}: {}", user.getEmail(), resetUrl);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getMail().getFrom());
        message.setTo(user.getEmail());
        message.setSubject("Reset your Tech Blog password");
        message.setText("""
                Hello %s,

                Open the link below to reset your password:
                %s

                This link will expire in %d minutes.
                """.formatted(user.getDisplayName(), resetUrl,
                appProperties.getPasswordReset().getExpirationMinutes()));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.warn("Could not send reset password email to {}. Reset link: {}", user.getEmail(), resetUrl, ex);
        }
    }
}
