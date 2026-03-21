package com.techblog.domain.notification.service;

import com.techblog.domain.notification.dto.NotificationListResponse;
import com.techblog.domain.user.model.User;

public interface NotificationService {

    void createNotification(User user, String type, String title, String message, String targetUrl);

    NotificationListResponse getMyNotifications(String email, int page, int size);

    void markAsRead(String email, Long notificationId);

    void markAllAsRead(String email);
}
