package com.techblog.domain.comment.dto;

import lombok.Data;

@Data
public class ResolveReportRequest {
    private String action; // e.g., "DISMISS", "HIDE", "DELETE"
    private String resolutionNote;
}
