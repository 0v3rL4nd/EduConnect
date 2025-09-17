package com.piattaforme.educonnect.persistence.repository;

import com.piattaforme.educonnect.persistence.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.isActive = true")
    List<Student> findActiveStudents();

    @Query("SELECT s FROM Student s WHERE s.educationLevel = :level")
    List<Student> findByEducationLevel(@Param("level") String level);

    @Query("SELECT s FROM Student s WHERE s.schoolName LIKE %:schoolName%")
    List<Student> findBySchoolName(@Param("schoolName") String schoolName);
}