package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.notification.NotificationDto;
import org.example.trangsuc_js.dto.notification.NotificationPreferencesDto;
import org.example.trangsuc_js.service.NotificationService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<NotificationDto> notifications = notificationService.getNotifications(pageable);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Notifications retrieved successfully",
            notifications
        ));
    }
    
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getUnreadNotifications() {
        List<NotificationDto> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Unread notifications retrieved successfully",
            notifications
        ));
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        Long count = notificationService.getUnreadCount();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Unread count retrieved successfully",
            count
        ));
    }
    
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationDto>> markAsRead(@PathVariable Long notificationId) {
        NotificationDto notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Notification marked as read successfully",
            notification
        ));
    }
    
    @PutMapping("/mark-all-read")
    public ResponseEntity<ApiResponse<String>> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "All notifications marked as read successfully",
            null
        ));
    }
    
    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferencesDto>> getNotificationPreferences() {
        NotificationPreferencesDto preferences = notificationService.getNotificationPreferences();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Notification preferences retrieved successfully",
            preferences
        ));
    }
    
    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferencesDto>> updateNotificationPreferences(
            @Valid @RequestBody NotificationPreferencesDto preferences) {
        NotificationPreferencesDto updated = notificationService.updateNotificationPreferences(preferences);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Notification preferences updated successfully",
            updated
        ));
    }
}
