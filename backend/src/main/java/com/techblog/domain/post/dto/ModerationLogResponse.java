package com.techblog.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationLogResponse {
    private Long id;
    private String postTitle;
    private String action;
    private String adminName;
    private String reason;
    private LocalDateTime createdAt;
}