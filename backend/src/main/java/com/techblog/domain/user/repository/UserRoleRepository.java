package com.techblog.domain.user.repository;

import com.techblog.domain.user.model.User;
import com.techblog.domain.user.model.UserRole;
import com.techblog.domain.user.model.UserRoleId;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    @EntityGraph(attributePaths = { "role" })
    List<UserRole> findByUser(User user);

    void deleteByUser(User user);
}
