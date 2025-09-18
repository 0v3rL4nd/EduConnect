package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.LessonType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LessonRequestDTO {

    // Getters and Setters
    @NotNull(message = "ID tutor è obbligatorio")
    private Long tutorId;

    @NotNull(message = "ID materia è obbligatorio")
    private Long subjectId;

    @NotNull(message = "Data/ora inizio è obbligatoria")
    private LocalDateTime startTime;

    @NotNull(message = "Data/ora fine è obbligatoria")
    private LocalDateTime endTime;

    @NotNull(message = "Tipo lezione è obbligatorio")
    private LessonType lessonType;

    private String location;
    private String meetingUrl;

    @NotNull(message = "Prezzo è obbligatorio")
    private BigDecimal price;

    private String description;

    // Constructors
    public LessonRequestDTO() {}

    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }

    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public void setLessonType(LessonType lessonType) { this.lessonType = lessonType; }

    public void setLocation(String location) { this.location = location; }

    public void setMeetingUrl(String meetingUrl) { this.meetingUrl = meetingUrl; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public void setDescription(String description) { this.description = description; }
}