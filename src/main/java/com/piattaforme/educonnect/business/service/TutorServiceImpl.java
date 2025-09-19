package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.business.dto.TutorDTO;
import com.piattaforme.educonnect.business.dto.TutorSearchDTO;
import com.piattaforme.educonnect.persistence.entity.CompetenceLevel;
import com.piattaforme.educonnect.persistence.entity.LessonType;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TutorServiceImpl implements TutorService{
    @Override
    public Optional<Tutor> getTutorById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Tutor> getAllTutors() {
        return List.of();
    }

    @Override
    public Tutor saveTutor(Tutor tutor) {
        return null;
    }

    @Override
    public void deleteTutor(Long id) {

    }

    @Override
    public List<Tutor> getAvailableTutors() {
        return List.of();
    }

    @Override
    public List<Tutor> searchTutors(TutorSearchDTO searchCriteria) {
        return List.of();
    }

    @Override
    public Page<Tutor> searchTutors(TutorSearchDTO searchCriteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<Tutor> getTutorsBySubject(String subjectName) {
        return List.of();
    }

    @Override
    public List<Tutor> getTutorsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return List.of();
    }

    @Override
    public List<Tutor> getTutorsByLocation(String city, String region) {
        return List.of();
    }

    @Override
    public List<Tutor> getTutorsByLessonType(LessonType lessonType) {
        return List.of();
    }

    @Override
    public List<Tutor> getTopRatedTutors(int limit) {
        return List.of();
    }

    @Override
    public List<Tutor> getMostExperiencedTutors(int limit) {
        return List.of();
    }

    @Override
    public Tutor updateTutorProfile(Long tutorId, TutorDTO tutorDTO) {
        return null;
    }

    @Override
    public Tutor addSubjectToTutor(Long tutorId, Long subjectId, CompetenceLevel level, BigDecimal hourlyRate) {
        return null;
    }

    @Override
    public void removeSubjectFromTutor(Long tutorId, Long subjectId) {

    }

    @Override
    public Tutor updateTutorAvailability(Long tutorId, boolean isAvailable) {
        return null;
    }

    @Override
    public void updateTutorRating(Long tutorId) {

    }

    @Override
    public BigDecimal calculateTutorAverageRating(Long tutorId) {
        return null;
    }

    @Override
    public boolean canTeachSubject(Long tutorId, Long subjectId) {
        return false;
    }

    @Override
    public List<String> validateTutorProfile(TutorDTO tutorDTO) {
        return List.of();
    }
}
