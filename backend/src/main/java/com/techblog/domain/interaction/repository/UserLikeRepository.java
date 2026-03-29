package com.techblog.domain.interaction.repository;

import com.techblog.common.enums.TargetType;
import com.techblog.domain.interaction.model.UserLike;
import com.techblog.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
    Optional<UserLike> findByUserAndTargetTypeAndTargetId(User user, TargetType targetType, Long targetId);
    
    long countByTargetTypeAndTargetId(TargetType targetType, Long targetId);
    
    boolean existsByUserAndTargetTypeAndTargetId(User user, TargetType targetType, Long targetId);
}
