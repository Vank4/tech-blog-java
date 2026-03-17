package com.techblog.domain.comment.repository;

import com.techblog.domain.comment.model.CommentModerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentModerationLogRepository extends JpaRepository<CommentModerationLog, Long> {
}
