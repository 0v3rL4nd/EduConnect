package com.piattaforme.educonnect.business.service;


import com.piattaforme.educonnect.persistence.entity.*;

import java.util.List;

public interface NotificationService {

    // Invio notifiche
    Notification sendNotification(Long userId, String title, String message, NotificationType type);

    void sendBookingConfirmationNotification(Booking booking);

    void sendBookingCancellationNotification(Booking booking);

    void sendPaymentConfirmationNotification(Payment payment);

    void sendLessonReminderNotification(Booking booking);

    void sendNewMessageNotification(Message message);

    void sendReviewReceivedNotification(Review review);

    // Gestione notifiche
    List<Notification> getNotificationsForUser(Long userId);

    List<Notification> getUnreadNotificationsForUser(Long userId);

    Long getUnreadNotificationCount(Long userId);

    void markNotificationAsRead(Long notificationId);

    void markAllNotificationsAsRead(Long userId);

    void deleteNotification(Long notificationId);
}