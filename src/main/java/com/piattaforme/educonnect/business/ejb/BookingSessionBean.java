package com.piattaforme.educonnect.business.ejb;

import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.persistence.repository.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BookingSessionBean {

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private LessonRepository lessonRepository;

    @Inject
    private StudentRepository studentRepository;

    @Inject
    private PaymentRepository paymentRepository;

    /**
     * Crea nuova prenotazione - transazione atomica complessa
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Booking createBooking(Long studentId, Long lessonId, String studentNotes) {
        // 1. Verifica esistenza studente
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Studente non trovato"));

        // 2. Verifica esistenza e disponibilità lezione
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lezione non trovata"));

        if (!lesson.isAvailable()) {
            throw new IllegalStateException("Lezione non più disponibile");
        }

        // 3. Verifica che la lezione sia futura
        if (lesson.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Impossibile prenotare lezioni passate");
        }

        // 4. Verifica che lo studente non abbia già prenotato questa lezione
        if (bookingRepository.findByStudentAndStatus(student, BookingStatus.PENDING).stream()
                .anyMatch(b -> b.getLesson().getId().equals(lessonId))) {
            throw new IllegalStateException("Prenotazione già esistente per questa lezione");
        }

        // 5. Creazione prenotazione
        Booking booking = new Booking(student, lesson, lesson.getPrice());
        booking.setStudentNotes(studentNotes);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        // 6. Aggiornamento stato lezione
        lesson.setStatus(LessonStatus.BOOKED);
        lessonRepository.save(lesson);

        return savedBooking;
    }

    /**
     * Conferma prenotazione - richiede transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Solo prenotazioni in attesa possono essere confermate");
        }

        // Verifica pagamento
        if (booking.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Pagamento non completato");
        }

        booking.confirm();
        return bookingRepository.save(booking);
    }

    /**
     * Cancella prenotazione - gestione transazione complessa
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Booking cancelBooking(Long bookingId, CancelledBy cancelledBy, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        if (!booking.canBeCancelled()) {
            throw new IllegalStateException("Prenotazione non può essere cancellata");
        }

        // Cancellazione prenotazione
        booking.cancel(cancelledBy, reason);
        Booking savedBooking = bookingRepository.save(booking);

        // Ripristino disponibilità lezione
        Lesson lesson = booking.getLesson();
        lesson.setStatus(LessonStatus.AVAILABLE);
        lessonRepository.save(lesson);

        // Gestione rimborso se necessario
        if (booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
            // Logica rimborso (da implementare con servizio pagamenti)
            Payment payment = booking.getPayment();
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }

        return savedBooking;
    }

    /**
     * Completa prenotazione - transazione atomica
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        if (!booking.canBeCompleted()) {
            throw new IllegalStateException("Prenotazione non può essere completata");
        }

        // Completamento prenotazione
        booking.complete();
        Booking savedBooking = bookingRepository.save(booking);

        // Aggiornamento statistiche tutor
        Tutor tutor = booking.getLesson().getTutor();
        tutor.setTotalLessons(tutor.getTotalLessons() + 1);

        // L'aggiornamento del tutor sarà gestito da altro servizio

        return savedBooking;
    }

    /**
     * Recupera prenotazioni per studente - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Booking> getBookingsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Studente non trovato"));

        return bookingRepository.findByStudentOrderByBookingDateDesc(student);
    }

    /**
     * Recupera prenotazioni per tutor - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Booking> getBookingsForTutor(Optional<Tutor> tutor) {
        return bookingRepository.findByTutorOrderByBookingDateDesc(tutor);
    }

    /**
     * Verifica possibilità prenotazione - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean canBookLesson(Long studentId, Long lessonId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        if (student == null || lesson == null) {
            return false;
        }

        // Verifica disponibilità lezione
        if (!lesson.isAvailable()) {
            return false;
        }

        // Verifica che la lezione sia futura
        if (lesson.getStartTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Verifica che non ci siano prenotazioni esistenti
        return bookingRepository.findByStudentAndStatus(student, BookingStatus.PENDING).stream()
                .noneMatch(b -> b.getLesson().getId().equals(lessonId));
    }


}
