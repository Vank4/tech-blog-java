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
public class VerificationEmailSender {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final AppProperties appProperties;

    public void sendVerificationEmail(User user, String verificationUrl) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null || !StringUtils.hasText(appProperties.getMail().getFrom())) {
            log.info("Mail sender is not configured. Verification link for {}: {}", user.getEmail(), verificationUrl);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getMail().getFrom());
        message.setTo(user.getEmail());
        message.setSubject("Verify your Tech Blog account");
        message.setText("""
                Hello %s,

                Please verify your email by opening the link below:
                %s

                This link will expire in %d minutes.
                """.formatted(user.getDisplayName(), verificationUrl,
                appProperties.getEmailVerification().getExpirationMinutes()));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.warn("Could not send verification email to {}. Verification link: {}", user.getEmail(), verificationUrl, ex);
        }
    }
}
