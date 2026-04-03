package com.techblog.domain.interaction.repository;

import com.techblog.common.enums.TargetType;
import com.techblog.domain.interaction.model.UserFavorite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {

    List<UserFavorite> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, TargetType targetType, Long targetId);
    
    java.util.Optional<UserFavorite> findByUserIdAndTargetTypeAndTargetId(Long userId, TargetType targetType, Long targetId);
    
    long countByTargetTypeAndTargetId(TargetType targetType, Long targetId);
}
