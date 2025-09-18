package com.piattaforme.educonnect.persistence.repository;



import com.piattaforme.educonnect.persistence.entity.Notification;
import com.piattaforme.educonnect.persistence.entity.NotificationType;
import com.piattaforme.educonnect.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notifiche per utente
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // Notifiche non lette
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadNotifications(@Param("user") User user);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.isRead = false")
    Long countUnreadNotifications(@Param("user") User user);

    // Notifiche per tipo
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.type = :type ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndType(@Param("user") User user, @Param("type") NotificationType type);

    // Notifiche recenti
    @Query("SELECT n FROM Notification n WHERE n.user = :user ORDER BY n.createdAt DESC")
    List<Notification> findRecentNotifications(@Param("user") User user, Pageable pageable);
}