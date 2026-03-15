package com.techblog.domain.user.repository;

import com.techblog.common.enums.RoleName;
import com.techblog.domain.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}