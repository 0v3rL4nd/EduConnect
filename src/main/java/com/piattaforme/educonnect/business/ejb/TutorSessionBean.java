package com.piattaforme.educonnect.business.ejb;

import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.persistence.repository.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TutorSessionBean {

    @Inject
    private TutorRepository tutorRepository;

    @Inject
    private SubjectRepository subjectRepository;

    @Inject
    private ReviewRepository reviewRepository;

    @Inject
    private LessonRepository lessonRepository;

    /**
     * Recupera tutti i tutor disponibili - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> getAvailableTutors() {
        return tutorRepository.findAvailableTutors();
    }

    /**
     * Aggiunge una materia al profilo tutor - transazione richiesta
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public TutorSubject addSubjectToTutor(Long tutorId, Long subjectId,
                                          CompetenceLevel competenceLevel,
                                          BigDecimal hourlyRate) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Tutor non trovato"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Materia non trovata"));

        // Verifica che non esista già questa combinazione
        boolean alreadyExists = tutor.getTutorSubjects().stream()
                .anyMatch(ts -> ts.getSubject().getId().equals(subjectId));

        if (alreadyExists) {
            throw new IllegalArgumentException("Tutor insegna già questa materia");
        }

        TutorSubject tutorSubject = new TutorSubject(tutor, subject, competenceLevel, hourlyRate);
        tutor.getTutorSubjects().add(tutorSubject);

        tutorRepository.save(tutor);

        return tutorSubject;
    }

    /**
     * Aggiorna il rating del tutor basato sulle recensioni - transazione richiesta
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateTutorRating(Long tutorId) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Tutor non trovato"));

        // Calcola nuovo rating
        BigDecimal averageRating = reviewRepository.getAverageRatingForTutor(tutor);
        Long totalReviews = reviewRepository.countReviewsForTutor(tutor);

        if (averageRating != null && totalReviews != null) {
            tutor.setRating(averageRating);
            tutor.setTotalReviews(totalReviews.intValue());

            tutorRepository.save(tutor);
        }
    }

    /**
     * Crea una nuova lezione per il tutor - transazione richiesta
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Lesson createLesson(Long tutorId, Long subjectId, Lesson lesson) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Tutor non trovato"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Materia non trovata"));

        // Verifica che il tutor possa insegnare questa materia
        boolean canTeach = tutor.getTutorSubjects().stream()
                .anyMatch(ts -> ts.getSubject().getId().equals(subjectId) && ts.getIsActive());

        if (!canTeach) {
            throw new IllegalArgumentException("Tutor non autorizzato a insegnare questa materia");
        }

        // Verifica conflitti orari
        List<Lesson> conflictingLessons = lessonRepository.findConflicting(
                tutor, lesson.getStartTime(), lesson.getEndTime());

        if (!conflictingLessons.isEmpty()) {
            throw new IllegalStateException("Conflitto orario con lezioni esistenti");
        }

        lesson.setTutor(tutor);
        lesson.setSubject(subject);
        lesson.setStatus(LessonStatus.AVAILABLE);

        return lessonRepository.save(lesson);
    }

    /**
     * Trova tutor per materia - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> findTutorsBySubject(String subjectName) {
        return tutorRepository.findBySubjectName(subjectName);
    }

    /**
     * Trova top tutor per rating - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> getTopRatedTutors(int limit) {
        return tutorRepository.findTopRatedTutors(5,
                org.springframework.data.domain.PageRequest.of(0, limit));
    }

    /**
     * Attiva/disattiva disponibilità tutor - transazione semplice
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateTutorAvailability(Long tutorId, boolean isAvailable) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("Tutor non trovato"));

        tutor.setIsAvailable(isAvailable);
        tutorRepository.save(tutor);
    }
}