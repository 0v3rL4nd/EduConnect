package com.piattaforme.educonnect.presentation.rest;

import com.piattaforme.educonnect.business.service.BookingService;
import com.piattaforme.educonnect.business.dto.*;
import com.piattaforme.educonnect.persistence.entity.Booking;
import com.piattaforme.educonnect.persistence.entity.BookingStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingRestController {

    @Autowired
    private BookingService bookingService;

    /**
     * Crea nuova prenotazione
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequest,
                                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        // Verifica che lo studente possa prenotare solo per se stesso
        if (!userId.equals(bookingRequest.getStudentId())) {
            return ResponseEntity.status(403).build();
        }

        try {
            Booking booking = bookingService.createBooking(bookingRequest);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Ottieni prenotazioni per utente corrente
     */
    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(HttpSession session,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Long userId = (Long) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");

        if (userId == null || userType == null) {
            return ResponseEntity.status(401).build();
        }

        Page<Booking> bookings;
        if ("STUDENT".equals(userType)) {
            bookings = bookingService.getBookingsForStudent(userId, PageRequest.of(page, size));
        } else if ("TUTOR".equals(userType)) {
            bookings = bookingService.getBookingsForTutor(userId, PageRequest.of(page, size));
        } else {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(bookings.getContent());
    }

    /**
     * Ottieni prenotazioni per status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable BookingStatus status,
                                                             HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");

        if (userId == null || userType == null) {
            return ResponseEntity.status(401).build();
        }

        List<Booking> bookings;
        if ("STUDENT".equals(userType)) {
            bookings = bookingService.getBookingsByStudentAndStatus(userId, status);
        } else if ("TUTOR".equals(userType)) {
            bookings = bookingService.getBookingsByTutorAndStatus(userId, status);
        } else {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(bookings);
    }

    /**
     * Conferma prenotazione (solo tutor)
     */
    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long bookingId,
                                                  HttpSession session) {
        String userType = (String) session.getAttribute("userType");

        if (!"TUTOR".equals(userType)) {
            return ResponseEntity.status(403).build();
        }

        try {
            Booking booking = bookingService.confirmBooking(bookingId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancella prenotazione
     */
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long bookingId,
                                                 @RequestParam String reason,
                                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");

        if (userId == null || userType == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            com.piattaforme.educonnect.persistence.entity.CancelledBy cancelledBy =
                    "STUDENT".equals(userType) ?
                            com.piattaforme.educonnect.persistence.entity.CancelledBy.STUDENT :
                            com.piattaforme.educonnect.persistence.entity.CancelledBy.TUTOR;

            Booking booking = bookingService.cancelBooking(bookingId, cancelledBy, reason);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Completa prenotazione (solo tutor)
     */
    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<Booking> completeBooking(@PathVariable Long bookingId,
                                                   HttpSession session) {
        String userType = (String) session.getAttribute("userType");

        if (!"TUTOR".equals(userType)) {
            return ResponseEntity.status(403).build();
        }

        try {
            Booking booking = bookingService.completeBooking(bookingId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
