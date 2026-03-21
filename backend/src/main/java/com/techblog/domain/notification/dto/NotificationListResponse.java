package com.techblog.domain.notification.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationListResponse {
    private List<NotificationResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private long unreadCount;
}
