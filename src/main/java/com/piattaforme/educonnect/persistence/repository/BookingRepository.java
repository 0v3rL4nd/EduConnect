package com.piattaforme.educonnect.persistence.repository;


import com.piattaforme.educonnect.persistence.entity.Booking;
import com.piattaforme.educonnect.persistence.entity.BookingStatus;
import com.piattaforme.educonnect.persistence.entity.Student;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Prenotazioni per studente
    List<Booking> findByStudentOrderByBookingDateDesc(Student student);

    Page<Booking> findByStudentOrderByBookingDateDesc(Student student, Pageable pageable);

    // Prenotazioni per tutor
    @Query("SELECT b FROM Booking b WHERE b.lesson.tutor = :tutor ORDER BY b.bookingDate DESC")
    List<Booking> findByTutorOrderByBookingDateDesc(@Param("tutor") Tutor tutor);

    @Query("SELECT b FROM Booking b WHERE b.lesson.tutor = :tutor ORDER BY b.bookingDate DESC")
    Page<Booking> findByTutorOrderByBookingDateDesc(@Param("tutor") Tutor tutor, Pageable pageable);

    // Prenotazioni per status
    List<Booking> findByStatusOrderByBookingDateDesc(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.student = :student AND b.status = :status ORDER BY b.bookingDate DESC")
    List<Booking> findByStudentAndStatus(@Param("student") Student student, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.lesson.tutor = :tutor AND b.status = :status ORDER BY b.bookingDate DESC")
    List<Booking> findByTutorAndStatus(@Param("tutor") Tutor tutor, @Param("status") BookingStatus status);

    // Prenotazioni in un range di date
    @Query("SELECT b FROM Booking b WHERE b.lesson.startTime BETWEEN :startDate AND :endDate ORDER BY b.lesson.startTime")
    List<Booking> findBookingsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    // Prenotazioni prossime per studente
    @Query("SELECT b FROM Booking b WHERE b.student = :student " +
            "AND b.status = 'CONFIRMED' AND b.lesson.startTime > CURRENT_TIMESTAMP " +
            "ORDER BY b.lesson.startTime ASC")
    List<Booking> findUpcomingBookingsForStudent(@Param("student") Student student);

    // Prenotazioni prossime per tutor
    @Query("SELECT b FROM Booking b WHERE b.lesson.tutor = :tutor " +
            "AND b.status = 'CONFIRMED' AND b.lesson.startTime > CURRENT_TIMESTAMP " +
            "ORDER BY b.lesson.startTime ASC")
    List<Booking> findUpcomingBookingsForTutor(@Param("tutor") Tutor tutor);

    // Statistiche
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.student = :student")
    Long countByStudent(@Param("student") Student student);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.lesson.tutor = :tutor")
    Long countByTutor(@Param("tutor") Tutor tutor);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status")
    Long countByStatus(@Param("status") BookingStatus status);
}