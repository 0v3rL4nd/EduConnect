package com.piattaforme.educonnect.presentation.controller;


import com.piattaforme.educonnect.business.service.*;
import com.piattaforme.educonnect.business.dto.*;
import com.piattaforme.educonnect.persistence.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private LessonService lessonService;

    /**
     * Dashboard studente
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("userId");

        // Prenotazioni prossime
        List<Booking> upcomingBookings = bookingService.getUpcomingBookingsForStudent(studentId);
        model.addAttribute("upcomingBookings", upcomingBookings);

        // Tutor raccomandati
        List<TutorMatchDTO> recommendedTutors = matchingService.recommendTutorsForStudent(studentId, 5);
        model.addAttribute("recommendedTutors", recommendedTutors);

        // Statistiche
        Long totalBookings = bookingService.countBookingsForStudent(studentId);
        model.addAttribute("totalBookings", totalBookings);

        return "student/dashboard";
    }

    /**
     * Ricerca tutor
     */
    @GetMapping("/search")
    public String searchTutors(@RequestParam(required = false) String subject,
                               @RequestParam(required = false) String location,
                               @RequestParam(required = false) String minPrice,
                               @RequestParam(required = false) String maxPrice,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {

        TutorSearchDTO searchCriteria = new TutorSearchDTO();
        searchCriteria.setSubjectName(subject);
        searchCriteria.setLocation(location);

        if (minPrice != null && !minPrice.isEmpty()) {
            searchCriteria.setMinPrice(new java.math.BigDecimal(minPrice));
        }
        if (maxPrice != null && !maxPrice.isEmpty()) {
            searchCriteria.setMaxPrice(new java.math.BigDecimal(maxPrice));
        }

        Page<Tutor> tutors = tutorService.searchTutors(searchCriteria, PageRequest.of(page, 12));

        model.addAttribute("tutors", tutors);
        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("currentPage", page);

        return "student/search";
    }

    /**
     * Dettagli tutor
     */
    @GetMapping("/tutor/{tutorId}")
    public String tutorDetails(@PathVariable Long tutorId, Model model) {
        Tutor tutor = tutorService.getTutorById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor non trovato"));

        // Lezioni disponibili del tutor
        List<Lesson> availableLessons = lessonService.getLessonsByTutor(tutorId);

        model.addAttribute("tutor", tutor);
        model.addAttribute("availableLessons", availableLessons);

        return "student/tutor-details";
    }

    /**
     * Le mie prenotazioni
     */
    @GetMapping("/bookings")
    public String myBookings(HttpSession session,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {

        Long studentId = (Long) session.getAttribute("userId");

        Page<Booking> bookings = bookingService.getBookingsForStudent(studentId,
                PageRequest.of(page, 10));

        model.addAttribute("bookings", bookings);
        model.addAttribute("currentPage", page);

        return "student/my-bookings";
    }

    /**
     * Cancella prenotazione
     */
    @PostMapping("/bookings/{bookingId}/cancel")
    public String cancelBooking(@PathVariable Long bookingId,
                                @RequestParam String reason,
                                HttpSession session,
                                Model model) {

        try {
            bookingService.cancelBooking(bookingId, CancelledBy.STUDENT, reason);
            model.addAttribute("success", "Prenotazione cancellata con successo");
        } catch (Exception e) {
            model.addAttribute("error", "Errore nella cancellazione: " + e.getMessage());
        }

        return "redirect:/student/bookings";
    }
}