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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private LessonService lessonService;

    /**
     * Dashboard tutor
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long tutorId = (Long) session.getAttribute("userId");

        // Prenotazioni prossime
        List<Booking> upcomingBookings = bookingService.getUpcomingBookingsForTutor(tutorId);
        model.addAttribute("upcomingBookings", upcomingBookings);

        // Statistiche tutor
        Long totalBookings = bookingService.countBookingsForTutor(tutorId);
        model.addAttribute("totalBookings", totalBookings);

        // Tutor info
        Tutor tutor = tutorService.getTutorById(tutorId).orElse(null);
        model.addAttribute("tutor", tutor);

        return "tutor/dashboard";
    }

    /**
     * Gestione profilo tutor
     */
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long tutorId = (Long) session.getAttribute("userId");

        Tutor tutor = tutorService.getTutorById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor non trovato"));

        model.addAttribute("tutor", tutor);

        return "tutor/profile";
    }

    /**
     * Aggiorna profilo tutor
     */
    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute TutorDTO tutorDTO,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Long tutorId = (Long) session.getAttribute("userId");

        try {
            tutorService.updateTutorProfile(tutorId, tutorDTO);
            redirectAttributes.addFlashAttribute("success", "Profilo aggiornato con successo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nell'aggiornamento: " + e.getMessage());
        }

        return "redirect:/tutor/profile";
    }

    /**
     * Gestione lezioni
     */
    @GetMapping("/lessons")
    public String lessons(HttpSession session,
                          @RequestParam(defaultValue = "0") int page,
                          Model model) {

        Long tutorId = (Long) session.getAttribute("userId");

        Page<Lesson> lessons = lessonService.getLessonsByTutor(tutorId,
                PageRequest.of(page, 15));

        model.addAttribute("lessons", lessons);
        model.addAttribute("currentPage", page);

        return "tutor/lessons";
    }

    /**
     * Crea nuova lezione
     */
    @GetMapping("/lessons/new")
    public String newLessonForm(Model model) {
        model.addAttribute("lessonRequest", new LessonRequestDTO());
        return "tutor/lesson-form";
    }

    /**
     * Salva nuova lezione
     */
    @PostMapping("/lessons/create")
    public String createLesson(@Valid @ModelAttribute LessonRequestDTO lessonRequest,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Long tutorId = (Long) session.getAttribute("userId");
        lessonRequest.setTutorId(tutorId);

        try {
            Lesson lesson = lessonService.createLesson(lessonRequest);
            redirectAttributes.addFlashAttribute("success", "Lezione creata con successo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella creazione: " + e.getMessage());
        }

        return "redirect:/tutor/lessons";
    }

    /**
     * Le mie prenotazioni ricevute
     */
    @GetMapping("/bookings")
    public String myBookings(HttpSession session,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {

        Long tutorId = (Long) session.getAttribute("userId");

        Page<Booking> bookings = bookingService.getBookingsForTutor(tutorId,
                PageRequest.of(page, 10));

        model.addAttribute("bookings", bookings);
        model.addAttribute("currentPage", page);

        return "tutor/my-bookings";
    }

    /**
     * Conferma prenotazione
     */
    @PostMapping("/bookings/{bookingId}/confirm")
    public String confirmBooking(@PathVariable Long bookingId,
                                 RedirectAttributes redirectAttributes) {

        try {
            bookingService.confirmBooking(bookingId);
            redirectAttributes.addFlashAttribute("success", "Prenotazione confermata!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella conferma: " + e.getMessage());
        }

        return "redirect:/tutor/bookings";
    }
}
