package com.piattaforme.educonnect.business.ejb;

import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.persistence.repository.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.UUID;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PaymentSessionBean {

    @Inject
    private PaymentRepository paymentRepository;

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private StudentRepository studentRepository;

    /**
     * Processa un pagamento - transazione atomica complessa
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment processPayment(Long bookingId, PaymentMethod paymentMethod,
                                  String externalPaymentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        if (booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Pagamento già completato");
        }

        // Crea nuovo pagamento
        Payment payment = new Payment(booking.getStudent(), booking,
                booking.getTotalAmount(), paymentMethod);
        payment.setExternalPaymentId(externalPaymentId);
        payment.setTransactionId(generateTransactionId());
        payment.setStatus(PaymentStatus.PROCESSING);

        Payment savedPayment = paymentRepository.save(payment);

        // Simula processamento pagamento esterno (in realtà chiamata a Stripe/PayPal)
        boolean paymentSuccessful = simulateExternalPayment(savedPayment);

        if (paymentSuccessful) {
            savedPayment.markAsProcessed();
            booking.setPaymentStatus(PaymentStatus.COMPLETED);
        } else {
            savedPayment.markAsFailed("Pagamento rifiutato dal provider");
            booking.setPaymentStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(savedPayment);
        bookingRepository.save(booking);

        return savedPayment;
    }

    /**
     * Gestisce rimborso - transazione richiesta
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment processRefund(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento non trovato"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Solo pagamenti completati possono essere rimborsati");
        }

        // Simula rimborso esterno
        boolean refundSuccessful = simulateExternalRefund(payment);

        if (refundSuccessful) {
            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setFailureReason(reason);

            // Aggiorna stato prenotazione
            Booking booking = payment.getBooking();
            booking.setPaymentStatus(PaymentStatus.REFUNDED);
            bookingRepository.save(booking);
        }

        return paymentRepository.save(payment);
    }

    /**
     * Verifica stato pagamento - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PaymentStatus checkPaymentStatus(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(Payment::getStatus)
                .orElse(null);
    }

    // Metodi privati per simulazione
    private String generateTransactionId() {
        return "TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private boolean simulateExternalPayment(Payment payment) {
        // Simula successo 90% delle volte
        return Math.random() > 0.1;
    }

    private boolean simulateExternalRefund(Payment payment) {
        // Simula successo rimborso 95% delle volte
        return Math.random() > 0.05;
    }
}