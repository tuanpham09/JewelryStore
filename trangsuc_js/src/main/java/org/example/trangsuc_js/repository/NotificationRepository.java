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
    
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    Page<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT n FROM Notification n WHERE n.user IS NULL ORDER BY n.createdAt DESC")
    Page<Notification> findGlobalNotifications(Pageable pageable);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
    Long countUnreadByUserId(@Param("userId") Long userId);
    
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NULL OR n.expiresAt > :now ORDER BY n.createdAt DESC")
    List<Notification> findActiveNotifications(@Param("now") LocalDateTime now);
    
    @Query("SELECT n FROM Notification n WHERE n.type = :type AND (n.user.id = :userId OR n.user IS NULL) ORDER BY n.createdAt DESC")
    List<Notification> findByTypeAndUserId(@Param("type") Notification.NotificationType type, @Param("userId") Long userId);
}
