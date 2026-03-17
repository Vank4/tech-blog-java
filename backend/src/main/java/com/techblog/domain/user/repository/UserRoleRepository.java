package com.techblog.domain.user.repository;

import com.techblog.domain.user.model.UserRole;
import com.techblog.domain.user.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}
