package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.BookingStatus;
import com.piattaforme.educonnect.persistence.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long lessonId;
    private String tutorName;
    private String subjectName;
    private LocalDateTime lessonStartTime;
    private LocalDateTime lessonEndTime;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private String studentNotes;
    private String tutorNotes;
    private String cancellationReason;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private String lessonLocation;
    private String meetingUrl;

    // Constructors
    public BookingDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public LocalDateTime getLessonStartTime() { return lessonStartTime; }
    public void setLessonStartTime(LocalDateTime lessonStartTime) { this.lessonStartTime = lessonStartTime; }

    public LocalDateTime getLessonEndTime() { return lessonEndTime; }
    public void setLessonEndTime(LocalDateTime lessonEndTime) { this.lessonEndTime = lessonEndTime; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getStudentNotes() { return studentNotes; }
    public void setStudentNotes(String studentNotes) { this.studentNotes = studentNotes; }

    public String getTutorNotes() { return tutorNotes; }
    public void setTutorNotes(String tutorNotes) { this.tutorNotes = tutorNotes; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public String getLessonLocation() { return lessonLocation; }
    public void setLessonLocation(String lessonLocation) { this.lessonLocation = lessonLocation; }

    public String getMeetingUrl() { return meetingUrl; }
    public void setMeetingUrl(String meetingUrl) { this.meetingUrl = meetingUrl; }
}