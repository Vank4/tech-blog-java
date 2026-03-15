package com.techblog.domain.comment.repository;

import com.techblog.common.enums.CommentTargetType;
import com.techblog.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTargetTypeAndTargetId(CommentTargetType targetType, Long targetId);
    List<Comment> findByParentId(Long parentId);
}