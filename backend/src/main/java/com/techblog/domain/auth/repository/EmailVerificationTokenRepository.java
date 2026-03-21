package com.techblog.domain.auth.repository;

import com.techblog.domain.auth.model.EmailVerificationToken;
import com.techblog.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByUser(User user);

    void deleteByUser(User user);
}
