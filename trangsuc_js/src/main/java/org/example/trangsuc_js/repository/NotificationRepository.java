package org.example.trangsuc_js.repository;

import org.example.trangsuc_js.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT n FROM Notification n WHERE n.user IS NULL ORDER BY n.createdAt DESC")
    Page<Notification> findGlobalNotifications(Pageable pageable);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.isRead = false")
    Long countUnreadByUserId(@Param("userId") Long userId);
    
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NULL OR n.expiresAt > :now ORDER BY n.createdAt DESC")
    List<Notification> findActiveNotifications(@Param("now") LocalDateTime now);
    
    @Query("SELECT n FROM Notification n WHERE n.type = :type AND (n.userId = :userId OR n.user IS NULL) ORDER BY n.createdAt DESC")
    List<Notification> findByTypeAndUserId(@Param("type") Notification.NotificationType type, @Param("userId") Long userId);
}
