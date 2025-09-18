package com.piattaforme.educonnect.persistence.repository;


import com.piattaforme.educonnect.persistence.entity.Subject;
import com.piattaforme.educonnect.persistence.entity.SubjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);

    List<Subject> findByCategory(SubjectCategory category);

    @Query("SELECT s FROM Subject s WHERE s.isActive = true ORDER BY s.name")
    List<Subject> findActiveSubjects();

    @Query("SELECT s FROM Subject s WHERE s.name LIKE %:name% AND s.isActive = true")
    List<Subject> findByNameContaining(@Param("name") String name);

    @Query("SELECT s FROM Subject s WHERE s.category = :category AND s.isActive = true ORDER BY s.name")
    List<Subject> findActiveByCategoryOrdered(@Param("category") SubjectCategory category);

    // Materie più popolari (con più tutor)
    @Query("SELECT s FROM Subject s " +
            "JOIN s.tutorSubjects ts " +
            "WHERE s.isActive = true " +
            "GROUP BY s " +
            "ORDER BY COUNT(ts) DESC")
    List<Subject> findMostPopularSubjects();
}