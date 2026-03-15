package com.techblog.domain.user.repository;

import com.techblog.domain.user.model.User;
import com.techblog.domain.user.model.UserRole;
import com.techblog.domain.user.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query("""
        select ur
        from UserRole ur
        join fetch ur.role
        where ur.user = :user
    """)
    List<UserRole> findByUser(@Param("user") User user);
}