package com.techblog.domain.analytics.repository;

import com.techblog.common.enums.TargetType;
import com.techblog.domain.analytics.model.PageView;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageViewRepository extends JpaRepository<PageView, Long> {

    long countByTargetTypeAndTargetIdAndViewedAtBetween(
            TargetType targetType,
            Long targetId,
            LocalDateTime start,
            LocalDateTime end);
}
