package com.piattaforme.educonnect.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "commission", precision = 10, scale = 2)
    private BigDecimal commission;

    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "external_payment_id")
    private String externalPaymentId;

    public Payment(Student student, Booking savedBooking, BigDecimal price, PaymentMethod selectedPaymentMethod) {
        this.student = student;
        this.booking = savedBooking;
        this.amount = price;
        this.paymentMethod = selectedPaymentMethod;
    }

    public Payment() {

    }

    public void setStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public void setExternalPaymentId(String externalPaymentId) {
        this.externalPaymentId = externalPaymentId;
    }

    public void setTransactionId(String s) {
        this.transactionId = s;
    }

    public void markAsProcessed() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void markAsFailed(String pagamentoRifiutatoDalProvider) {
        this.status = PaymentStatus.FAILED;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setFailureReason(String reason) {
        this.status = PaymentStatus.FAILED;
    }

    public Booking getBooking() {
        return booking;
    }
}