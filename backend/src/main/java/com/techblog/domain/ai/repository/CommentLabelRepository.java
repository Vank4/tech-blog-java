package com.techblog.domain.ai.repository;

import com.techblog.domain.ai.model.CommentLabel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLabelRepository extends JpaRepository<CommentLabel, Long> {
}
