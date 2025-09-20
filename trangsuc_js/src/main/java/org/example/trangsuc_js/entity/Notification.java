package org.example.trangsuc_js.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user; // null = notification cho tất cả users

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "action_url")
    private String actionUrl; // URL để redirect khi click notification

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON data cho các thông tin bổ sung

    public enum NotificationType {
        ORDER_STATUS, PROMOTION, PRODUCT_DISCOUNT, SYSTEM_ANNOUNCEMENT, REVIEW_APPROVED
    }

    public enum NotificationStatus {
        UNREAD, READ, ARCHIVED
    }
}
