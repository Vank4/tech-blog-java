package com.techblog.domain.post.repository;

import com.techblog.domain.post.model.PostModerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostModerationLogRepository extends JpaRepository<PostModerationLog, Long> {
}
