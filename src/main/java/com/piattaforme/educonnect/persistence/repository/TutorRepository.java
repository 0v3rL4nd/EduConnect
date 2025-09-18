package com.piattaforme.educonnect.persistence.repository;


import com.piattaforme.educonnect.persistence.entity.LessonType;
import com.piattaforme.educonnect.persistence.entity.Subject;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import com.piattaforme.educonnect.persistence.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    // Ricerca base tutor disponibili
    @Query("SELECT t FROM Tutor t WHERE t.isActive = true AND t.isAvailable = true")
    List<Tutor> findAvailableTutors();

    // Ricerca tutor per materia
    @Query("SELECT DISTINCT t FROM Tutor t " +
            "JOIN t.tutorSubjects ts " +
            "WHERE ts.subject = :subject AND t.isActive = true AND t.isAvailable = true")
    List<Tutor> findBySubject(@Param("subject") Subject subject);

    // Ricerca tutor per materia con paginazione
    @Query("SELECT DISTINCT t FROM Tutor t " +
            "JOIN t.tutorSubjects ts " +
            "WHERE ts.subject = :subject AND t.isActive = true AND t.isAvailable = true")
    Page<Tutor> findBySubject(@Param("subject") Subject subject, Pageable pageable);

    // Ricerca per nome materia
    @Query("SELECT DISTINCT t FROM Tutor t " +
            "JOIN t.tutorSubjects ts " +
            "JOIN ts.subject s " +
            "WHERE s.name = :subjectName AND t.isActive = true AND t.isAvailable = true")
    List<Tutor> findBySubjectName(@Param("subjectName") String subjectName);

    // Ricerca per range di prezzo
    @Query("SELECT DISTINCT t FROM Tutor t " +
            "JOIN t.tutorSubjects ts " +
            "WHERE ts.hourlyRate BETWEEN :minPrice AND :maxPrice " +
            "AND t.isActive = true AND t.isAvailable = true")
    List<Tutor> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);

    // Ricerca per località
    @Query("SELECT t FROM Tutor t " +
            "WHERE (t.locationCity LIKE %:city% OR t.locationRegion LIKE %:region%) " +
            "AND t.isActive = true AND t.isAvailable = true")
    List<Tutor> findByLocation(@Param("city") String city, @Param("region") String region);

    // Ricerca per tipo di lezione
    @Query("SELECT DISTINCT t FROM Tutor t " +
            "WHERE (:lessonType = 'ONLINE' AND t.canTeachOnline = true) " +
            "OR (:lessonType = 'IN_PERSON' AND t.canTeachInPerson = true) " +
            "AND t.isActive = true AND t.isAvailable = true")
    List<Tutor> findByLessonType(@Param("lessonType") LessonType lessonType);

    // Ricerca combinata avanzata
    @Query("SELECT DISTINCT t FROM Tutor t " +
            "JOIN t.tutorSubjects ts " +
            "JOIN ts.subject s " +
            "WHERE (:subjectName IS NULL OR s.name = :subjectName) " +
            "AND (:minPrice IS NULL OR ts.hourlyRate >= :minPrice) " +
            "AND (:maxPrice IS NULL OR ts.hourlyRate <= :maxPrice) " +
            "AND (:minRating IS NULL OR t.rating >= :minRating) " +
            "AND (:location IS NULL OR t.locationCity LIKE %:location% OR t.locationRegion LIKE %:location%) " +
            "AND t.isActive = true AND t.isAvailable = true " +
            "ORDER BY t.rating DESC, t.totalReviews DESC")
    Page<Tutor> searchTutors(@Param("subjectName") String subjectName,
                             @Param("minPrice") BigDecimal minPrice,
                             @Param("maxPrice") BigDecimal maxPrice,
                             @Param("minRating") BigDecimal minRating,
                             @Param("location") String location,
                             Pageable pageable);

    // Top tutor per rating
    @Query("SELECT t FROM Tutor t " +
            "WHERE t.isActive = true AND t.isAvailable = true AND t.totalReviews >= :minReviews " +
            "ORDER BY t.rating DESC, t.totalReviews DESC")
    List<Tutor> findTopRatedTutors(@Param("minReviews") Integer minReviews, Pageable pageable);

    // Tutor con più lezioni
    @Query("SELECT t FROM Tutor t " +
            "WHERE t.isActive = true " +
            "ORDER BY t.totalLessons DESC")
    List<Tutor> findMostExperiencedTutors(Pageable pageable);

    // Controllo conflitti orari per tutor
    @Query("SELECT l FROM Lesson l WHERE l.tutor = :tutor " +
            "AND l.status IN ('AVAILABLE', 'BOOKED') " +
            "AND ((l.startTime <= :endTime AND l.endTime >= :startTime))")
    List<Lesson> findConflicting(@Param("tutor") Tutor tutor,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);

    // Controllo conflitti per tutor e materia specifica
    @Query("SELECT l FROM Lesson l WHERE l.tutor = :tutor AND l.subject = :subject " +
            "AND l.status IN ('AVAILABLE', 'BOOKED') " +
            "AND ((l.startTime <= :endTime AND l.endTime >= :startTime))")
    List<Lesson> findConflictingByTutorAndSubject(@Param("tutor") Tutor tutor,
                                                  @Param("subject") Subject subject,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
}

