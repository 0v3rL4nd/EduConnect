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
public class BookingSeviceImpl implements BookingService {
    @Override
    public Optional<Booking> getBookingById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Booking> getAllBookings() {
        return List.of();
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

    }

    @Override
    public Booking createBooking(BookingRequestDTO bookingRequest) {
        return null;
    }

    @Override
    public Booking confirmBooking(Long bookingId) {
        return null;
    }

    @Override
    public Booking cancelBooking(Long bookingId, CancelledBy cancelledBy, String reason) {
        return null;
    }

    @Override
    public Booking completeBooking(Long bookingId) {
        return null;
    }

    @Override
    public List<Booking> getBookingsForStudent(Long studentId) {
        return List.of();
    }

    @Override
    public Page<Booking> getBookingsForStudent(Long studentId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Booking> getBookingsForTutor(Long tutorId) {
        return List.of();
    }

    @Override
    public Page<Booking> getBookingsForTutor(Long tutorId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByStudentAndStatus(Long studentId, BookingStatus status) {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsByTutorAndStatus(Long tutorId, BookingStatus status) {
        return List.of();
    }

    @Override
    public List<Booking> getBookingsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
    }

    @Override
    public List<Booking> getUpcomingBookingsForStudent(Long studentId) {
        return List.of();
    }

    @Override
    public List<Booking> getUpcomingBookingsForTutor(Long tutorId) {
        return List.of();
    }

    @Override
    public Long countBookingsForStudent(Long studentId) {
        return 0L;
    }

    @Override
    public Long countBookingsForTutor(Long tutorId) {
        return 0L;
    }

    @Override
    public Long countBookingsByStatus(BookingStatus status) {
        return 0L;
    }

    @Override
    public boolean canBookLesson(Long studentId, Long lessonId) {
        return false;
    }

    @Override
    public boolean canCancelBooking(Long bookingId, Long userId) {
        return false;
    }

    @Override
    public List<String> validateBookingRequest(BookingRequestDTO request) {
        return List.of();
    }
}
