package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.persistence.repository.*;
import com.piattaforme.educonnect.business.dto.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
@Transactional(readOnly = true)
public class MatchingServiceImpl implements MatchingService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<TutorMatchDTO> findMatchingTutors(Long studentId, MatchingCriteriaDTO criteria) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return new ArrayList<>();
        }

        // 1. Filtro base per materia
        List<Tutor> candidateTutors = criteria.getSubjectName() != null ?
                tutorRepository.findBySubjectName(criteria.getSubjectName()) :
                tutorRepository.findAvailableTutors();

        // 2. Applica filtri aggiuntivi
        candidateTutors = applyFilters(candidateTutors, criteria);

        // 3. Calcola score di compatibilità e ordina
        List<TutorMatchDTO> matches = candidateTutors.stream()
                .map(tutor -> createTutorMatch(tutor, student, criteria))
                .sorted((a, b) -> Double.compare(b.getCompatibilityScore(), a.getCompatibilityScore()))
                .collect(Collectors.toList());

        return matches;
    }

    @Override
    public List<TutorMatchDTO> recommendTutorsForStudent(Long studentId, int limit) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return new ArrayList<>();
        }

        // Crea criteri basati sulla storia dello studente
        MatchingCriteriaDTO criteria = createCriteriaFromHistory(student);

        return findMatchingTutors(studentId, criteria)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public double calculateCompatibilityScore(Student student, Tutor tutor, MatchingCriteriaDTO criteria) {
        double score = 0.0;
        double maxScore = 0.0;

        // 1. Rating del tutor (peso: 25%)
        if (tutor.getRating() != null) {
            score += (tutor.getRating().doubleValue() / 5.0) * 0.25;
        }
        maxScore += 0.25;

        // 2. Prezzo compatibile (peso: 20%)
        if (criteria.getMaxPrice() != null && tutor.getHourlyRate() != null) {
            if (tutor.getHourlyRate().compareTo(criteria.getMaxPrice()) <= 0) {
                double priceScore = 1.0 - (tutor.getHourlyRate().doubleValue() / criteria.getMaxPrice().doubleValue());
                score += priceScore * 0.20;
            }
        }
        maxScore += 0.20;

        // 3. Posizione geografica (peso: 15%)
        if (criteria.getPreferredLocation() != null) {
            if (locationMatches(tutor, criteria.getPreferredLocation())) {
                score += 0.15;
            }
        }
        maxScore += 0.15;

        // 4. Tipo di lezione (peso: 15%)
        if (criteria.getPreferredLessonType() != null) {
            if (supportsLessonType(tutor, criteria.getPreferredLessonType())) {
                score += 0.15;
            }
        }
        maxScore += 0.15;

        // 5. Esperienza tutor (peso: 15%)
        if (tutor.getTeachingExperience() != null) {
            double experienceScore = Math.min(tutor.getTeachingExperience().doubleValue() / 10.0, 1.0);
            score += experienceScore * 0.15;
        }
        maxScore += 0.15;

        // 6. Numero recensioni (peso: 10%)
        if (tutor.getTotalReviews() != null && tutor.getTotalReviews() > 0) {
            double reviewScore = Math.min(tutor.getTotalReviews().doubleValue() / 50.0, 1.0);
            score += reviewScore * 0.10;
        }
        maxScore += 0.10;

        return maxScore > 0 ? score / maxScore : 0.0;
    }

    @Override
    public List<Tutor> filterTutorsByAvailability(List<Tutor> tutors, List<String> preferredTimeSlots) {
        // Implementazione filtro disponibilità basato su time slots
        // Per semplicità, ritorna tutti i tutor attivi
        return tutors.stream()
                .filter(Tutor::getIsAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tutor> filterTutorsByLocation(List<Tutor> tutors, String location, int maxDistanceKm) {
        // Implementazione filtro geografico
        // In un'implementazione reale, useremmo coordinate GPS e calcolo distanza
        return tutors.stream()
                .filter(tutor -> locationMatches(tutor, location))
                .collect(Collectors.toList());
    }

    @Override
    public List<Subject> getRecommendedSubjectsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return new ArrayList<>();
        }

        // Analizza le prenotazioni passate per raccomandare materie correlate
        List<Booking> pastBookings = bookingRepository.findByStudentOrderByBookingDateDesc(student);

        Set<String> studiedSubjects = pastBookings.stream()
                .map(booking -> booking.getLesson().getSubject().getName())
                .collect(Collectors.toSet());

        // Logica semplificata: raccomanda materie della stessa categoria
        // In un sistema reale, si userebbe machine learning
        return new ArrayList<>(); // Placeholder
    }

    @Override
    public List<Tutor> getSimilarTutors(Long tutorId, int limit) {
        Tutor referenceTutor = tutorRepository.findById(tutorId).orElse(null);
        if (referenceTutor == null) {
            return new ArrayList<>();
        }

        // Trova tutor con materie simili
        Set<String> referenceSubjects = referenceTutor.getTutorSubjects().stream()
                .map(ts -> ts.getSubject().getName())
                .collect(Collectors.toSet());

        return tutorRepository.findAvailableTutors().stream()
                .filter(tutor -> !tutor.getId().equals(tutorId))
                .filter(tutor -> hasCommonSubjects(tutor, referenceSubjects))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Metodi helper privati
    private List<Tutor> applyFilters(List<Tutor> tutors, MatchingCriteriaDTO criteria) {
        return tutors.stream()
                .filter(tutor -> {
                    // Filtro prezzo
                    if (criteria.getMaxPrice() != null && tutor.getHourlyRate() != null) {
                        if (tutor.getHourlyRate().compareTo(criteria.getMaxPrice()) > 0) {
                            return false;
                        }
                    }

                    // Filtro rating minimo
                    if (criteria.getMinRating() != null && tutor.getRating() != null) {
                        if (tutor.getRating().compareTo(criteria.getMinRating()) < 0) {
                            return false;
                        }
                    }

                    // Filtro tipo lezione
                    if (criteria.getPreferredLessonType() != null) {
                        if (!supportsLessonType(tutor, criteria.getPreferredLessonType())) {
                            return false;
                        }
                    }

                    return tutor.getIsAvailable();
                })
                .collect(Collectors.toList());
    }

    private TutorMatchDTO createTutorMatch(Tutor tutor, Student student, MatchingCriteriaDTO criteria) {
        TutorMatchDTO match = new TutorMatchDTO();
        match.setTutorId(tutor.getId());
        match.setTutorName(tutor.getFullName());
        match.setBio(tutor.getBio());
        match.setHourlyRate(tutor.getHourlyRate());
        match.setRating(tutor.getRating());
        match.setTotalReviews(tutor.getTotalReviews());
        match.setLocation(tutor.getLocationCity() + ", " + tutor.getLocationRegion());
        match.setCanTeachOnline(tutor.getCanTeachOnline());
        match.setCanTeachInPerson(tutor.getCanTeachInPerson());
        match.setProfileImageUrl(tutor.getProfileImageUrl());

        double score = calculateCompatibilityScore(student, tutor, criteria);
        match.setCompatibilityScore(score);
        match.setMatchReason(generateMatchReason(tutor, criteria, score));

        return match;
    }

    private MatchingCriteriaDTO createCriteriaFromHistory(Student student) {
        MatchingCriteriaDTO criteria = new MatchingCriteriaDTO();

        // Analizza le prenotazioni passate
        List<Booking> pastBookings = bookingRepository.findByStudentOrderByBookingDateDesc(student);

        if (!pastBookings.isEmpty()) {
            // Determina materia più frequente
            Map<String, Long> subjectFrequency = pastBookings.stream()
                    .collect(Collectors.groupingBy(
                            booking -> booking.getLesson().getSubject().getName(),
                            Collectors.counting()
                    ));

            Optional<String> mostFrequentSubject = subjectFrequency.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);

            mostFrequentSubject.ifPresent(criteria::setSubjectName);

            // Calcola prezzo medio accettato
            double avgPrice = pastBookings.stream()
                    .mapToDouble(booking -> booking.getTotalAmount().doubleValue())
                    .average()
                    .orElse(50.0);

            criteria.setMaxPrice(java.math.BigDecimal.valueOf(avgPrice * 1.2)); // +20% del prezzo medio
        }

        return criteria;
    }

    private boolean locationMatches(Tutor tutor, String preferredLocation) {
        if (tutor.getLocationCity() == null) return false;
        return tutor.getLocationCity().toLowerCase().contains(preferredLocation.toLowerCase()) ||
                (tutor.getLocationRegion() != null &&
                        tutor.getLocationRegion().toLowerCase().contains(preferredLocation.toLowerCase()));
    }

    private boolean supportsLessonType(Tutor tutor, LessonType lessonType) {
        switch (lessonType) {
            case ONLINE:
                return tutor.getCanTeachOnline();
            case IN_PERSON:
                return tutor.getCanTeachInPerson();
            case HYBRID:
                return tutor.getCanTeachOnline() && tutor.getCanTeachInPerson();
            default:
                return true;
        }
    }

    private boolean hasCommonSubjects(Tutor tutor, Set<String> referenceSubjects) {
        Set<String> tutorSubjects = tutor.getTutorSubjects().stream()
                .map(ts -> ts.getSubject().getName())
                .collect(Collectors.toSet());

        return !Collections.disjoint(tutorSubjects, referenceSubjects);
    }

    private String generateMatchReason(Tutor tutor, MatchingCriteriaDTO criteria, double score) {
        List<String> reasons = new ArrayList<>();

        if (tutor.getRating() != null && tutor.getRating().doubleValue() >= 4.5) {
            reasons.add("Ottimo rating (" + tutor.getRating() + "/5)");
        }

        if (tutor.getTotalReviews() != null && tutor.getTotalReviews() > 20) {
            reasons.add("Molto recensito (" + tutor.getTotalReviews() + " recensioni)");
        }

        if (tutor.getTeachingExperience() != null && tutor.getTeachingExperience() > 5) {
            reasons.add("Esperienza consolidata (" + tutor.getTeachingExperience() + " anni)");
        }

        if (criteria.getPreferredLocation() != null && locationMatches(tutor, criteria.getPreferredLocation())) {
            reasons.add("Nella tua zona");
        }

        if (reasons.isEmpty()) {
            reasons.add("Profilo compatibile");
        }

        return String.join(", ", reasons);
    }

}


