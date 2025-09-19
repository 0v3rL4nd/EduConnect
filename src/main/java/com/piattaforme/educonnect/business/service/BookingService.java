package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.business.dto.BookingRequestDTO;
import com.piattaforme.educonnect.persistence.entity.Booking;
import com.piattaforme.educonnect.persistence.entity.BookingStatus;
import com.piattaforme.educonnect.persistence.entity.CancelledBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface BookingService {

    // Operazioni CRUD base
    Optional<Booking> getBookingById(Long id);

    List<Booking> getAllBookings();

    Booking saveBooking(Booking booking);

    void deleteBooking(Long id);

    // Creazione prenotazioni
    Booking createBooking(BookingRequestDTO bookingRequest);

    Booking confirmBooking(Long bookingId);

    Booking cancelBooking(Long bookingId, CancelledBy cancelledBy, String reason);

    Booking completeBooking(Long bookingId);

    // Recupero prenotazioni per utente
    List<Booking> getBookingsForStudent(Long studentId);

    Page<Booking> getBookingsForStudent(Long studentId, Pageable pageable);

    List<Booking> getBookingsForTutor(Long tutorId);

    Page<Booking> getBookingsForTutor(Long tutorId, Pageable pageable);

    // Prenotazioni per status
    List<Booking> getBookingsByStatus(BookingStatus status);

    List<Booking> getBookingsByStudentAndStatus(Long studentId, BookingStatus status);

    List<Booking> getBookingsByTutorAndStatus(Long tutorId, BookingStatus status);

    // Prenotazioni per data
    List<Booking> getBookingsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> getUpcomingBookingsForStudent(Long studentId);

    List<Booking> getUpcomingBookingsForTutor(Long tutorId);

    // Statistiche prenotazioni
    Long countBookingsForStudent(Long studentId);

    Long countBookingsForTutor(Long tutorId);

    Long countBookingsByStatus(BookingStatus status);

    // Validazioni
    boolean canBookLesson(Long studentId, Long lessonId);

    boolean canCancelBooking(Long bookingId, Long userId);

    List<String> validateBookingRequest(BookingRequestDTO request);
}
