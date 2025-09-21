package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.notification.NotificationDto;
import org.example.trangsuc_js.dto.notification.NotificationPreferencesDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(Pageable pageable);
    List<NotificationDto> getUnreadNotifications();
    Long getUnreadCount();
    NotificationDto markAsRead(Long notificationId);
    void markAllAsRead();
    NotificationPreferencesDto getNotificationPreferences();
    NotificationPreferencesDto updateNotificationPreferences(NotificationPreferencesDto preferences);
    void sendOrderStatusNotification(Long orderId, String status);
    void sendPromotionNotification(String title, String message, String actionUrl);
    void sendPriceDropNotification(Long productId, String productName, String oldPrice, String newPrice);
}
