package com.techblog.domain.comment.repository;

import com.techblog.common.enums.CommentStatus;
import com.techblog.common.enums.TargetType;
import com.techblog.domain.comment.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTargetTypeAndTargetIdAndStatusOrderByCreatedAtAsc(
            TargetType targetType,
            Long targetId,
            CommentStatus status);
}
