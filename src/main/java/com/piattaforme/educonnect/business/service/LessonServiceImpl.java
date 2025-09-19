package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.business.dto.LessonDTO;
import com.piattaforme.educonnect.business.dto.LessonRequestDTO;
import com.piattaforme.educonnect.persistence.entity.Lesson;
import com.piattaforme.educonnect.persistence.entity.LessonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService{
    @Override
    public Optional<Lesson> getLessonById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Lesson> getAllLessons() {
        return List.of();
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        return null;
    }

    @Override
    public void deleteLesson(Long id) {

    }

    @Override
    public Lesson createLesson(LessonRequestDTO lessonRequest) {
        return null;
    }

    @Override
    public Lesson updateLesson(Long lessonId, LessonDTO lessonDTO) {
        return null;
    }

    @Override
    public void cancelLesson(Long lessonId, String reason) {

    }

    @Override
    public List<Lesson> getAvailableLessons() {
        return List.of();
    }

    @Override
    public List<Lesson> getLessonsByTutor(Long tutorId) {
        return List.of();
    }

    @Override
    public Page<Lesson> getLessonsByTutor(Long tutorId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Lesson> getLessonsBySubject(String subjectName) {
        return List.of();
    }

    @Override
    public List<Lesson> getLessonsByType(LessonType lessonType) {
        return List.of();
    }

    @Override
    public List<Lesson> getLessonsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
    }

    @Override
    public List<Lesson> getAvailableLessonsByTutorAndSubject(Long tutorId, Long subjectId) {
        return List.of();
    }

    @Override
    public boolean isLessonAvailable(Long lessonId) {
        return false;
    }

    @Override
    public boolean hasConflictingLessons(Long tutorId, LocalDateTime startTime, LocalDateTime endTime) {
        return false;
    }

    @Override
    public List<Lesson> getConflictingLessons(Long tutorId, LocalDateTime startTime, LocalDateTime endTime) {
        return List.of();
    }

    @Override
    public List<String> validateLessonRequest(LessonRequestDTO request) {
        return List.of();
    }

    @Override
    public boolean canCreateLesson(Long tutorId, LocalDateTime startTime, LocalDateTime endTime) {
        return false;
    }
}
