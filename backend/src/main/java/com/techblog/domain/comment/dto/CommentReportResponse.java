package com.techblog.domain.comment.dto;

import com.techblog.common.enums.ReportStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentReportResponse {
    private Long id;
    private Long commentId;
    private String commentContent;
    private Long reporterId;
    private String reporterName;
    private String reason;
    private String detail;
    private ReportStatus status;
    private LocalDateTime createdAt;
    private String resolvedByName;
    private LocalDateTime resolvedAt;
    private String resolutionNote;
}
