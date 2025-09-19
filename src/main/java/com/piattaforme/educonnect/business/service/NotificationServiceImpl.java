package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.persistence.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public Notification sendNotification(Long userId, String title, String message, NotificationType type) {
        return null;
    }

    @Override
    public void sendBookingConfirmationNotification(Booking booking) {

    }

    @Override
    public void sendBookingCancellationNotification(Booking booking) {

    }

    @Override
    public void sendPaymentConfirmationNotification(Payment payment) {

    }

    @Override
    public void sendLessonReminderNotification(Booking booking) {

    }

    @Override
    public void sendNewMessageNotification(Message message) {

    }

    @Override
    public void sendReviewReceivedNotification(Review review) {

    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        return List.of();
    }

    @Override
    public List<Notification> getUnreadNotificationsForUser(Long userId) {
        return List.of();
    }

    @Override
    public Long getUnreadNotificationCount(Long userId) {
        return 0L;
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {

    }

    @Override
    public void markAllNotificationsAsRead(Long userId) {

    }

    @Override
    public void deleteNotification(Long notificationId) {

    }
}
