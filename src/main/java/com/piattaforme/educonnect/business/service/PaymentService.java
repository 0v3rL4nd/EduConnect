package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.business.dto.PaymentRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentService {

    // Operazioni CRUD base
    Optional<Payment> getPaymentById(Long id);

    List<Payment> getAllPayments();

    Payment savePayment(Payment payment);

    // Processamento pagamenti
    Payment processPayment(PaymentRequestDTO paymentRequest);

    Payment confirmPayment(String transactionId);

    Payment refundPayment(Long paymentId, String reason);

    // Recupero pagamenti
    List<Payment> getPaymentsByStudent(Long studentId);

    List<Payment> getPaymentsByStatus(PaymentStatus status);

    Optional<Payment> getPaymentByTransactionId(String transactionId);

    Optional<Payment> getPaymentByExternalId(String externalId);

    // Statistiche finanziarie
    BigDecimal getTotalAmountForStudent(Long studentId);

    BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    BigDecimal getTotalCommissions(LocalDateTime startDate, LocalDateTime endDate);

    // Validazioni
    boolean canProcessPayment(PaymentRequestDTO request);

    List<String> validatePaymentRequest(PaymentRequestDTO request);
}