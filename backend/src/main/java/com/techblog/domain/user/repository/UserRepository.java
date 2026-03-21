package com.techblog.domain.user.repository;

import com.techblog.common.enums.UserStatus;
import com.techblog.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = { "userRoles", "userRoles.role" })
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = { "userRoles", "userRoles.role" })
    Optional<User> findByEmailOrUsername(String email, String username);

    @EntityGraph(attributePaths = { "userRoles", "userRoles.role" })
    @Query("""
            select u from User u
            where (:keyword is null or lower(u.email) like lower(concat('%', :keyword, '%'))
                or lower(u.displayName) like lower(concat('%', :keyword, '%')))
              and (:status is null or u.status = :status)
            """)
    Page<User> searchUsers(@Param("keyword") String keyword, @Param("status") UserStatus status, Pageable pageable);

    @EntityGraph(attributePaths = { "userRoles", "userRoles.role" })
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
