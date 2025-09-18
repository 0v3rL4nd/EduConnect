package com.piattaforme.educonnect.persistence.repository;

import com.piattaforme.educonnect.persistence.entity.Review;
import com.piattaforme.educonnect.persistence.entity.Student;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Recensioni per tutor
    List<Review> findByTutorAndIsVisibleTrueOrderByCreatedAtDesc(Tutor tutor);

    Page<Review> findByTutorAndIsVisibleTrueOrderByCreatedAtDesc(Tutor tutor, Pageable pageable);

    // Recensioni scritte da studente
    List<Review> findByStudentOrderByCreatedAtDesc(Student student);

    // Recensioni per rating
    @Query("SELECT r FROM Review r WHERE r.tutor = :tutor AND r.rating >= :minRating AND r.isVisible = true ORDER BY r.createdAt DESC")
    List<Review> findByTutorAndMinRating(@Param("tutor") Tutor tutor, @Param("minRating") BigDecimal minRating);

    // Statistiche recensioni
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tutor = :tutor AND r.isVisible = true")
    BigDecimal getAverageRatingForTutor(@Param("tutor") Tutor tutor);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.tutor = :tutor AND r.isVisible = true")
    Long countReviewsForTutor(@Param("tutor") Tutor tutor);

    // Ultime recensioni per tutor
    @Query("SELECT r FROM Review r WHERE r.tutor = :tutor AND r.isVisible = true ORDER BY r.createdAt DESC")
    List<Review> findRecentReviewsForTutor(@Param("tutor") Tutor tutor, Pageable pageable);
}
