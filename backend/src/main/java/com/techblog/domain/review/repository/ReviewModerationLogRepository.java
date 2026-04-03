package com.techblog.domain.review.repository;

import com.techblog.domain.review.model.ReviewModerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewModerationLogRepository extends JpaRepository<ReviewModerationLog, Long> {
}
