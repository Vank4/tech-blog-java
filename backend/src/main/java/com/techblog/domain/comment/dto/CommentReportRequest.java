package com.techblog.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentReportRequest {
    @NotBlank(message = "Lý do báo cáo không được để trống")
    private String reason;

    private String detail;
}
