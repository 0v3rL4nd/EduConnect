package com.piattaforme.educonnect.persistence.repository;


import com.piattaforme.educonnect.persistence.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    // Lezioni disponibili
    @Query("SELECT l FROM Lesson l WHERE l.status = 'AVAILABLE' AND l.startTime > CURRENT_TIMESTAMP ORDER BY l.startTime")
    List<Lesson> findAvailableLessons();

    // Lezioni per tutor
    List<Lesson> findByTutorOrderByStartTimeDesc(Tutor tutor);

    Page<Lesson> findByTutorOrderByStartTimeDesc(Tutor tutor, Pageable pageable);

    // Lezioni per materia
    List<Lesson> findBySubjectAndStatusOrderByStartTime(Subject subject, LessonStatus status);

    // Lezioni in un range temporale
    @Query("SELECT l FROM Lesson l WHERE l.startTime BETWEEN :startTime AND :endTime ORDER BY l.startTime")
    List<Lesson> findLessonsBetweenDates(@Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    // Lezioni disponibili per tutor e materia
    @Query("SELECT l FROM Lesson l WHERE l.tutor = :tutor AND l.subject = :subject " +
            "AND l.status = 'AVAILABLE' AND l.startTime > CURRENT_TIMESTAMP ORDER BY l.startTime")
    List<Lesson> findAvailableLessonsByTutorAndSubject(@Param("tutor") Tutor tutor, @Param("subject") Subject subject);

    // Lezioni per tipo
    @Query("SELECT l FROM Lesson l WHERE l.lessonType = :lessonType AND l.status = 'AVAILABLE' " +
            "AND l.startTime > CURRENT_TIMESTAMP ORDER BY l.startTime")
    List<Lesson> findAvailableLessonsByType(@Param("lessonType") LessonType lessonType);

    // Controllo conflitti orari per tutor
    @Query("SELECT l FROM Lesson l WHERE l.tutor = :tutor " +
            "AND l.status IN ('AVAILABLE', 'BOOKED') " +
            "AND ((l.startTime <= :endTime AND l.endTime >= :startTime))")
    List<Lesson> findConflicting  (@Param("tutor") Tutor tutor, @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);

    // Controllo conflitti orari per tutor e materia
    @Query("SELECT l FROM Lesson l WHERE l.tutor = :tutor AND l.subject = :subject " +
            "AND l.status IN ('AVAILABLE', 'BOOKED') " +
            "AND ((l.startTime <= :endTime AND l.endTime >= :startTime))")
    List<Lesson> findConflictingByTutorAndSubject(@Param("tutor") Tutor tutor, @Param("subject") Subject subject,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
}