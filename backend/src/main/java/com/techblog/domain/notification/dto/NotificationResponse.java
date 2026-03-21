package com.techblog.domain.notification.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String type;
    private String title;
    private String message;
    private String targetUrl;
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
}
