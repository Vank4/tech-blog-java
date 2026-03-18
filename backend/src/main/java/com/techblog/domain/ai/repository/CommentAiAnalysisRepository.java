package com.techblog.domain.ai.repository;

import com.techblog.domain.ai.model.CommentAiAnalysis;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentAiAnalysisRepository extends JpaRepository<CommentAiAnalysis, Long> {

    List<CommentAiAnalysis> findByCommentIdOrderByAnalyzedAtDesc(Long commentId);
}
