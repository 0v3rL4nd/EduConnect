package com.piattaforme.educonnect.business.ejb;

import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.persistence.repository.*;
import com.piattaforme.educonnect.business.dto.BookingCartItem;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.Remove;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StatefulBookingBean {

    private Long studentId;
    private List<BookingCartItem> cartItems = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private PaymentMethod selectedPaymentMethod;
    private String specialNotes;

    @Inject
    private StudentRepository studentRepository;

    @Inject
    private LessonRepository lessonRepository;

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private PaymentRepository paymentRepository;



    /**
     * Inizializza la sessione di prenotazione per uno studente
     */
    public void initializeBookingSession(Long studentId) {
        this.studentId = studentId;
        this.cartItems.clear();
        this.totalAmount = BigDecimal.ZERO;
        this.selectedPaymentMethod = null;
        this.specialNotes = null;
    }

    /**
     * Aggiunge una lezione al carrello di prenotazione
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean addLessonToCart(Long lessonId) {
        // Verifica che la lezione non sia già nel carrello
        if (cartItems.stream().anyMatch(item -> item.getLessonId().equals(lessonId))) {
            return false; // Già presente
        }

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson == null || !lesson.isAvailable()) {
            return false;
        }

        // Aggiunge al carrello
        BookingCartItem cartItem = new BookingCartItem();
        cartItem.setLessonId(lessonId);
        cartItem.setTutorName(lesson.getTutor().getFullName());
        cartItem.setSubjectName(lesson.getSubject().getName());
        cartItem.setStartTime(lesson.getStartTime());
        cartItem.setEndTime(lesson.getEndTime());
        cartItem.setPrice(lesson.getPrice());

        cartItems.add(cartItem);

        // Ricalcola totale
        recalculateTotal();

        return true;
    }

    /**
     * Rimuove una lezione dal carrello
     */
    public boolean removeLessonFromCart(Long lessonId) {
        boolean removed = cartItems.removeIf(item -> item.getLessonId().equals(lessonId));
        if (removed) {
            recalculateTotal();
        }
        return removed;
    }

    /**
     * Ottiene gli elementi nel carrello
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<BookingCartItem> getCartItems() {
        return new ArrayList<>(cartItems); // Copia difensiva
    }

    /**
     * Ottiene il totale corrente
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Imposta il metodo di pagamento
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.selectedPaymentMethod = paymentMethod;
    }

    /**
     * Imposta note speciali per la prenotazione
     */
    public void setSpecialNotes(String notes) {
        this.specialNotes = notes;
    }

    /**
     * Verifica se il carrello è pronto per il checkout
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isReadyForCheckout() {
        return !cartItems.isEmpty() &&
                selectedPaymentMethod != null &&
                studentId != null;
    }

    /**
     * Esegue il checkout - transazione atomica complessa
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Booking> checkout() {
        if (!isReadyForCheckout()) {
            throw new IllegalStateException("Carrello non pronto per il checkout");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Studente non trovato"));

        List<Booking> createdBookings = new ArrayList<>();

        // Processa ogni elemento del carrello
        for (BookingCartItem item : cartItems) {
            // Verifica disponibilità lezione (controllo finale)
            Lesson lesson = lessonRepository.findById(item.getLessonId())
                    .orElseThrow(() -> new IllegalStateException("Lezione non trovata: " + item.getLessonId()));

            if (!lesson.isAvailable()) {
                throw new IllegalStateException("Lezione non più disponibile: " + lesson.getSubject().getName());
            }

            // Crea prenotazione
            Booking booking = new Booking(student, lesson, lesson.getPrice());
            booking.setStudentNotes(specialNotes);
            booking.setStatus(BookingStatus.PENDING);

            Booking savedBooking = bookingRepository.save(booking);
            createdBookings.add(savedBooking);

            // Aggiorna stato lezione
            lesson.setStatus(LessonStatus.BOOKED);
            lessonRepository.save(lesson);

            // Crea record di pagamento
            Payment payment = new Payment(student, savedBooking, lesson.getPrice(), selectedPaymentMethod);
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);
        }

        return createdBookings;
    }

    /**
     * Svuota il carrello
     */
    public void clearCart() {
        cartItems.clear();
        totalAmount = BigDecimal.ZERO;
        selectedPaymentMethod = null;
        specialNotes = null;
    }

    /**
     * Metodo di cleanup - rimuove il bean quando non più necessario
     */
    @Remove
    public void endSession() {
        clearCart();
        studentId = null;
    }

    /**
     * Ricalcola il totale del carrello
     */
    private void recalculateTotal() {
        totalAmount = cartItems.stream()
                .map(BookingCartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verifica conflitti orari nel carrello
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean hasTimeConflicts() {
        for (int i = 0; i < cartItems.size(); i++) {
            for (int j = i + 1; j < cartItems.size(); j++) {
                BookingCartItem item1 = cartItems.get(i);
                BookingCartItem item2 = cartItems.get(j);

                // Verifica sovrapposizione orari
                if (item1.getStartTime().isBefore(item2.getEndTime()) &&
                        item2.getStartTime().isBefore(item1.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Ottiene informazioni sulla sessione corrente
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public String getSessionInfo() {
        return String.format("Sessione Studente ID: %d, Items nel carrello: %d, Totale: €%.2f",
                studentId, cartItems.size(), totalAmount);
    }
}