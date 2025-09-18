package com.piattaforme.educonnect.persistence.repository;


import com.piattaforme.educonnect.persistence.entity.Payment;
import com.piattaforme.educonnect.persistence.entity.PaymentStatus;
import com.piattaforme.educonnect.persistence.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Pagamenti per studente
    List<Payment> findByStudentOrderByCreatedAtDesc(Student student);

    Page<Payment> findByStudentOrderByCreatedAtDesc(Student student, Pageable pageable);

    // Pagamenti per status
    List<Payment> findByStatusOrderByCreatedAtDesc(PaymentStatus status);

    // Pagamento per transaction ID
    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByExternalPaymentId(String externalPaymentId);

    // Statistiche pagamenti
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.student = :student AND p.status = com.piattaforme.educonnect.persistence.entity.PaymentStatus.COMPLETED")
    BigDecimal getTotalAmountByStudent(@Param("student") Student student);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = com.piattaforme.educonnect.persistence.entity.PaymentStatus.COMPLETED")
    BigDecimal getTotalCompletedAmount();

    // Pagamenti falliti
    @Query("SELECT p FROM Payment p WHERE p.status = com.piattaforme.educonnect.persistence.entity.PaymentStatus.FAILED")
    List<Payment> findFailedPayments();
}