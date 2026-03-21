package com.techblog.domain.notification.service.impl;

import com.techblog.domain.notification.dto.NotificationListResponse;
import com.techblog.domain.notification.dto.NotificationResponse;
import com.techblog.domain.notification.model.Notification;
import com.techblog.domain.notification.repository.NotificationRepository;
import com.techblog.domain.notification.service.NotificationService;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createNotification(User user, String type, String title, String message, String targetUrl) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTargetUrl(targetUrl);
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponse getMyNotifications(String email, int page, int size) {
        User user = findUserByEmail(email);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

        return NotificationListResponse.builder()
                .content(notifications.getContent().stream().map(this::mapToResponse).toList())
                .page(notifications.getNumber())
                .size(notifications.getSize())
                .totalElements(notifications.getTotalElements())
                .unreadCount(notificationRepository.countByUserIdAndReadFalse(user.getId()))
                .build();
    }

    @Override
    @Transactional
    public void markAsRead(String email, Long notificationId) {
        User user = findUserByEmail(email);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("You do not have permission to update this notification");
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(String email) {
        User user = findUserByEmail(email);
        notificationRepository.markAllAsReadByUserId(user.getId());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .targetUrl(notification.getTargetUrl())
                .read(notification.isRead())
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
