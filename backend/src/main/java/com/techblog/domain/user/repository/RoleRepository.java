package com.techblog.domain.user.repository;

import com.techblog.common.enums.RoleName;
import com.techblog.domain.user.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
