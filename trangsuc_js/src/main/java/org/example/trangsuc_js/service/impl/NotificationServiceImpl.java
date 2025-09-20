package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.notification.NotificationDto;
import org.example.trangsuc_js.dto.notification.NotificationPreferencesDto;
import org.example.trangsuc_js.entity.Notification;
import org.example.trangsuc_js.entity.User;
import org.example.trangsuc_js.repository.NotificationRepository;
import org.example.trangsuc_js.repository.UserRepository;
import org.example.trangsuc_js.service.NotificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(Pageable pageable) {
        User user = getCurrentUser();
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .getContent()
                .stream()
                .map(this::toNotificationDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadNotifications() {
        User user = getCurrentUser();
        return notificationRepo.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toNotificationDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getUnreadCount() {
        User user = getCurrentUser();
        return notificationRepo.countUnreadByUserId(user.getId());
    }
    
    @Override
    @Transactional
    public NotificationDto markAsRead(Long notificationId) {
        User user = getCurrentUser();
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        // Check if user owns this notification
        if (notification.getUser() != null && !notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notification.setStatus(Notification.NotificationStatus.READ);
        notification.setUpdatedAt(LocalDateTime.now());
        
        notificationRepo.save(notification);
        return toNotificationDto(notification);
    }
    
    @Override
    @Transactional
    public void markAllAsRead() {
        User user = getCurrentUser();
        List<Notification> unreadNotifications = notificationRepo.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId());
        
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification.setStatus(Notification.NotificationStatus.READ);
            notification.setUpdatedAt(LocalDateTime.now());
        }
        
        notificationRepo.saveAll(unreadNotifications);
    }
    
    @Override
    @Transactional(readOnly = true)
    public NotificationPreferencesDto getNotificationPreferences() {
        User user = getCurrentUser();
        // For now, return default preferences. In production, you would store these in database
        NotificationPreferencesDto preferences = new NotificationPreferencesDto();
        preferences.setEmailNotifications(true);
        preferences.setSmsNotifications(false);
        preferences.setPushNotifications(true);
        preferences.setOrderStatusUpdates(true);
        preferences.setPromotionNotifications(true);
        preferences.setPriceDropAlerts(true);
        preferences.setSystemAnnouncements(true);
        preferences.setReviewReminders(true);
        return preferences;
    }
    
    @Override
    @Transactional
    public NotificationPreferencesDto updateNotificationPreferences(NotificationPreferencesDto preferences) {
        User user = getCurrentUser();
        // In production, you would save these preferences to database
        // For now, just return the updated preferences
        return preferences;
    }
    
    @Override
    @Transactional
    public void sendOrderStatusNotification(Long orderId, String status) {
        User user = getCurrentUser();
        
        String title = "Cập nhật trạng thái đơn hàng";
        String message = "Đơn hàng #" + orderId + " đã được cập nhật trạng thái: " + getStatusDescription(status);
        String actionUrl = "/orders/" + orderId;
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.ORDER_STATUS);
        notification.setStatus(Notification.NotificationStatus.UNREAD);
        notification.setActionUrl(actionUrl);
        notification.setCreatedAt(LocalDateTime.now());
        
        notificationRepo.save(notification);
    }
    
    @Override
    @Transactional
    public void sendPromotionNotification(String title, String message, String actionUrl) {
        User user = getCurrentUser();
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.PROMOTION);
        notification.setStatus(Notification.NotificationStatus.UNREAD);
        notification.setActionUrl(actionUrl);
        notification.setCreatedAt(LocalDateTime.now());
        
        notificationRepo.save(notification);
    }
    
    @Override
    @Transactional
    public void sendPriceDropNotification(Long productId, String productName, String oldPrice, String newPrice) {
        User user = getCurrentUser();
        
        String title = "Giá sản phẩm giảm!";
        String message = "Sản phẩm " + productName + " đã giảm giá từ " + oldPrice + " xuống " + newPrice;
        String actionUrl = "/products/" + productId;
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.PRODUCT_DISCOUNT);
        notification.setStatus(Notification.NotificationStatus.UNREAD);
        notification.setActionUrl(actionUrl);
        notification.setCreatedAt(LocalDateTime.now());
        
        notificationRepo.save(notification);
    }
    
    private NotificationDto toNotificationDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType().name());
        dto.setStatus(notification.getStatus().name());
        dto.setIsRead(notification.getIsRead());
        dto.setReadAt(notification.getReadAt());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setExpiresAt(notification.getExpiresAt());
        dto.setActionUrl(notification.getActionUrl());
        dto.setMetadata(notification.getMetadata());
        return dto;
    }
    
    private String getStatusDescription(String status) {
        switch (status.toUpperCase()) {
            case "PENDING":
                return "Đang chờ xử lý";
            case "PAID":
                return "Đã thanh toán";
            case "PROCESSING":
                return "Đang xử lý";
            case "SHIPPED":
                return "Đã gửi hàng";
            case "DELIVERED":
                return "Đã giao hàng";
            case "CANCELLED":
                return "Đã hủy";
            case "REFUNDED":
                return "Đã hoàn tiền";
            default:
                return "Không xác định";
        }
    }
}
