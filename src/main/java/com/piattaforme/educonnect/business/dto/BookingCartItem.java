package com.piattaforme.educonnect.business.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class BookingCartItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Getters and Setters
    private Long lessonId;
    private String tutorName;
    private String subjectName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private String lessonType;
    private String location;
    private String meetingUrl;
    private String description;
    private Long tutorId;
    private Long subjectId;

    // Constructors
    public BookingCartItem() {}

    public BookingCartItem(Long lessonId, String tutorName, String subjectName,
                           LocalDateTime startTime, LocalDateTime endTime, BigDecimal price) {
        this.lessonId = lessonId;
        this.tutorName = tutorName;
        this.subjectName = subjectName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    // Business methods
    public long getDurationMinutes() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMinutes();
        }
        return 0;
    }

    public boolean hasTimeConflictWith(BookingCartItem other) {
        if (this.startTime == null || this.endTime == null ||
                other.startTime == null || other.endTime == null) {
            return false;
        }

        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    // toString per debug
    @Override
    public String toString() {
        return String.format("BookingCartItem{lessonId=%d, tutor='%s', subject='%s', startTime=%s, price=€%.2f}",
                lessonId, tutorName, subjectName, startTime, price);
    }

    // equals e hashCode basati su lessonId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingCartItem that = (BookingCartItem) o;
        return Objects.equals(lessonId, that.lessonId);
    }

    @Override
    public int hashCode() {
        return lessonId != null ? lessonId.hashCode() : 0;
    }
}