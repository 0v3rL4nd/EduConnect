package com.piattaforme.educonnect.business.service;


import com.piattaforme.educonnect.business.dto.TutorDTO;
import com.piattaforme.educonnect.business.dto.TutorSearchDTO;
import com.piattaforme.educonnect.persistence.entity.LessonType;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import com.piattaforme.educonnect.persistence.entity.CompetenceLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TutorService {

    // Operazioni CRUD base
    Optional<Tutor> getTutorById(Long id);

    List<Tutor> getAllTutors();

    Tutor saveTutor(Tutor tutor);

    void deleteTutor(Long id);

    // Ricerca tutor
    List<Tutor> getAvailableTutors();

    List<Tutor> searchTutors(TutorSearchDTO searchCriteria);

    Page<Tutor> searchTutors(TutorSearchDTO searchCriteria, Pageable pageable);

    List<Tutor> getTutorsBySubject(String subjectName);

    List<Tutor> getTutorsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<Tutor> getTutorsByLocation(String city, String region);

    List<Tutor> getTutorsByLessonType(LessonType lessonType);

    // Top tutor
    List<Tutor> getTopRatedTutors(int limit);

    List<Tutor> getMostExperiencedTutors(int limit);

    // Gestione profilo tutor
    Tutor updateTutorProfile(Long tutorId, TutorDTO tutorDTO);

    Tutor addSubjectToTutor(Long tutorId, Long subjectId, CompetenceLevel level, BigDecimal hourlyRate);

    void removeSubjectFromTutor(Long tutorId, Long subjectId);

    Tutor updateTutorAvailability(Long tutorId, boolean isAvailable);

    // Rating e recensioni
    void updateTutorRating(Long tutorId);

    BigDecimal calculateTutorAverageRating(Long tutorId);

    // Validazioni
    boolean canTeachSubject(Long tutorId, Long subjectId);

    List<String> validateTutorProfile(TutorDTO tutorDTO);
}
