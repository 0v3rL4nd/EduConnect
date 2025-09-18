package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.business.dto.LessonRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.piattaforme.educonnect.business.dto.LessonDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LessonService {

    // Operazioni CRUD base
    Optional<Lesson> getLessonById(Long id);

    List<Lesson> getAllLessons();

    Lesson saveLesson(Lesson lesson);

    void deleteLesson(Long id);

    // Creazione e gestione lezioni
    Lesson createLesson(LessonRequestDTO lessonRequest);

    Lesson updateLesson(Long lessonId, LessonDTO lessonDTO);

    void cancelLesson(Long lessonId, String reason);

    // Ricerca lezioni
    List<Lesson> getAvailableLessons();

    List<Lesson> getLessonsByTutor(Long tutorId);

    Page<Lesson> getLessonsByTutor(Long tutorId, Pageable pageable);

    List<Lesson> getLessonsBySubject(String subjectName);

    List<Lesson> getLessonsByType(LessonType lessonType);

    List<Lesson> getLessonsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<Lesson> getAvailableLessonsByTutorAndSubject(Long tutorId, Long subjectId);

    // Controllo disponibilità
    boolean isLessonAvailable(Long lessonId);

    boolean hasConflictingLessons(Long tutorId, LocalDateTime startTime, LocalDateTime endTime);

    List<Lesson> getConflictingLessons(Long tutorId, LocalDateTime startTime, LocalDateTime endTime);

    // Validazioni
    List<String> validateLessonRequest(LessonRequestDTO request);

    boolean canCreateLesson(Long tutorId, LocalDateTime startTime, LocalDateTime endTime);
}