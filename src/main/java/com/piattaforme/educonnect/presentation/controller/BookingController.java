package com.piattaforme.educonnect.presentation.controller;

import com.piattaforme.educonnect.business.service.*;
import com.piattaforme.educonnect.business.dto.*;
import com.piattaforme.educonnect.business.ejb.StatefulBookingBean;
import com.piattaforme.educonnect.persistence.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private PaymentService paymentService;

     @Inject
    private StatefulBookingBean bookingCartBean;


    /**
     * Aggiungi lezione al carrello
     */
    @PostMapping("/cart/add/{lessonId}")
    @ResponseBody
    public String addToCart(@PathVariable Long lessonId, HttpSession session) {
        Long studentId = (Long) session.getAttribute("userId");

        if (studentId == null) {
            return "ERROR: Non autenticato";
        }

        try {
            // Inizializza sessione carrello se necessario
            if (session.getAttribute("cartInitialized") == null) {
                bookingCartBean.initializeBookingSession(studentId);
                session.setAttribute("cartInitialized", true);
            }

            boolean added = bookingCartBean.addLessonToCart(lessonId);

            if (added) {
                return "SUCCESS: Lezione aggiunta al carrello";
            } else {
                return "ERROR: Lezione già presente o non disponibile";
            }

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Visualizza carrello
     */
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("userId");

        if (studentId == null) {
            return "redirect:/auth/login";
        }

        try {
            List<BookingCartItem> cartItems = bookingCartBean.getCartItems();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", bookingCartBean.getTotalAmount());
            model.addAttribute("hasTimeConflicts", bookingCartBean.hasTimeConflicts());
            model.addAttribute("isReadyForCheckout", bookingCartBean.isReadyForCheckout());

        } catch (Exception e) {
            model.addAttribute("error", "Errore nel recupero carrello: " + e.getMessage());
        }

        return "booking/cart";
    }

    /**
     * Rimuovi lezione dal carrello
     */
    @PostMapping("/cart/remove/{lessonId}")
    public String removeFromCart(@PathVariable Long lessonId,
                                 RedirectAttributes redirectAttributes) {

        try {
            boolean removed = bookingCartBean.removeLessonFromCart(lessonId);

            if (removed) {
                redirectAttributes.addFlashAttribute("success", "Lezione rimossa dal carrello");
            } else {
                redirectAttributes.addFlashAttribute("error", "Lezione non trovata nel carrello");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore: " + e.getMessage());
        }

        return "redirect:/booking/cart";
    }

    /**
     * Pagina checkout
     */
    @GetMapping("/checkout")
    public String checkout(Model model) {
        try {
            if (!bookingCartBean.isReadyForCheckout()) {
                model.addAttribute("error", "Carrello non pronto per il checkout");
                return "booking/cart";
            }

            List<BookingCartItem> cartItems = bookingCartBean.getCartItems();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", bookingCartBean.getTotalAmount());
            model.addAttribute("paymentRequest", new PaymentRequestDTO());

        } catch (Exception e) {
            model.addAttribute("error", "Errore nel checkout: " + e.getMessage());
            return "booking/cart";
        }

        return "booking/checkout";
    }

    /**
     * Processa checkout
     */
    @PostMapping("/checkout/process")
    public String processCheckout(@ModelAttribute PaymentRequestDTO paymentRequest,
                                  RedirectAttributes redirectAttributes) {

        try {
            // Imposta metodo pagamento nel carrello
            bookingCartBean.setPaymentMethod(paymentRequest.getPaymentMethod());

            // Esegue checkout
            List<Booking> bookings = bookingCartBean.checkout();

            // Processa pagamenti per ogni prenotazione
            for (Booking booking : bookings) {
                paymentRequest.setBookingId(booking.getId());
                paymentRequest.setAmount(booking.getTotalAmount());
                paymentService.processPayment(paymentRequest);
            }

            // Svuota carrello
            bookingCartBean.clearCart();

            redirectAttributes.addFlashAttribute("success",
                    "Prenotazioni completate! Hai prenotato " + bookings.size() + " lezioni.");

            return "redirect:/student/bookings";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nel checkout: " + e.getMessage());
            return "redirect:/booking/checkout";
        }
    }

    /**
     * Prenotazione singola (senza carrello)
     */
    @PostMapping("/quick-book/{lessonId}")
    public String quickBook(@PathVariable Long lessonId,
                            @RequestParam(required = false) String notes,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Long studentId = (Long) session.getAttribute("userId");

        if (studentId == null) {
            return "redirect:/auth/login";
        }

        try {
            BookingRequestDTO bookingRequest = new BookingRequestDTO(studentId, lessonId);
            bookingRequest.setStudentNotes(notes);

            Booking booking = bookingService.createBooking(bookingRequest);

            redirectAttributes.addFlashAttribute("success",
                    "Prenotazione creata! Completa il pagamento per confermare.");
            redirectAttributes.addFlashAttribute("bookingId", booking.getId());

            return "redirect:/booking/payment/" + booking.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella prenotazione: " + e.getMessage());
            return "redirect:/student/search";
        }
    }

    /**
     * Pagina pagamento singolo
     */
    @GetMapping("/payment/{bookingId}")
    public String paymentPage(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setBookingId(bookingId);
        paymentRequest.setAmount(booking.getTotalAmount());

        model.addAttribute("booking", booking);
        model.addAttribute("paymentRequest", paymentRequest);

        return "booking/payment";
    }
}