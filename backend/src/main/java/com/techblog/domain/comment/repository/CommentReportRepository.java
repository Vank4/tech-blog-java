package com.techblog.domain.comment.repository;

import com.techblog.common.enums.ReportStatus;
import com.techblog.domain.comment.model.CommentReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    List<CommentReport> findAllByOrderByCreatedAtDesc();
    
    List<CommentReport> findByStatusOrderByCreatedAtDesc(ReportStatus status);
}
