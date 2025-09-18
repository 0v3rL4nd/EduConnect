package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.LessonStatus;
import com.piattaforme.educonnect.persistence.entity.LessonType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LessonDTO {
    private Long id;
    private Long tutorId;
    private String tutorName;
    private Long subjectId;
    private String subjectName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LessonType lessonType;
    private String location;
    private String meetingUrl;
    private BigDecimal price;
    private String description;
    private LessonStatus status;
    private Boolean isAvailable;
    private Long durationMinutes;

    // Constructors
    public LessonDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }

    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public LessonType getLessonType() { return lessonType; }
    public void setLessonType(LessonType lessonType) { this.lessonType = lessonType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getMeetingUrl() { return meetingUrl; }
    public void setMeetingUrl(String meetingUrl) { this.meetingUrl = meetingUrl; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LessonStatus getStatus() { return status; }
    public void setStatus(LessonStatus status) { this.status = status; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public Long getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Long durationMinutes) { this.durationMinutes = durationMinutes; }
}