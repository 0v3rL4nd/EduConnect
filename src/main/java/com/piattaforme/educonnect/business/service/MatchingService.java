package com.piattaforme.educonnect.business.service;


import com.piattaforme.educonnect.business.dto.MatchingCriteriaDTO;
import com.piattaforme.educonnect.business.dto.TutorMatchDTO;
import com.piattaforme.educonnect.persistence.entity.*;


import java.util.List;

public interface MatchingService {

    // Matching principale
    List<TutorMatchDTO> findMatchingTutors(Long studentId, MatchingCriteriaDTO criteria);

    List<TutorMatchDTO> recommendTutorsForStudent(Long studentId, int limit);

    // Algoritmi di matching
    double calculateCompatibilityScore(Student student, Tutor tutor, MatchingCriteriaDTO criteria);

    List<Tutor> filterTutorsByAvailability(List<Tutor> tutors, List<String> preferredTimeSlots);

    List<Tutor> filterTutorsByLocation(List<Tutor> tutors, String location, int maxDistanceKm);

    // Raccomandazioni personalizzate
    List<Subject> getRecommendedSubjectsForStudent(Long studentId);

    List<Tutor> getSimilarTutors(Long tutorId, int limit);
}