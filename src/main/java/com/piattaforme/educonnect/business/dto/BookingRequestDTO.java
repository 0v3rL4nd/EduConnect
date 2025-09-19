package com.piattaforme.educonnect.business.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookingRequestDTO {

    @NotNull(message = "ID studente è obbligatorio")
    private Long studentId;

    @NotNull(message = "ID lezione è obbligatorio")
    private Long lessonId;

    private String studentNotes;

    private String specialRequests;

    // Constructors
    public BookingRequestDTO() {}

    public BookingRequestDTO(Long studentId, Long lessonId) {
        this.studentId = studentId;
        this.lessonId = lessonId;
    }

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getStudentNotes() { return studentNotes; }
    public void setStudentNotes(String studentNotes) { this.studentNotes = studentNotes; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}