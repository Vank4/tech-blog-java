package com.techblog.domain.user.repository;

import com.techblog.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = { "userRoles", "userRoles.role" })
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = { "userRoles", "userRoles.role" })
    Optional<User> findByEmailOrUsername(String email, String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
