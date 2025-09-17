package com.piattaforme.educonnect.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "tutor_subjects")
public class TutorSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "competence_level")
    @Enumerated(EnumType.STRING)
    private CompetenceLevel competenceLevel;

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    @DecimalMin(value = "5.0", message = "Tariffa oraria minima 5€")
    @DecimalMax(value = "200.0", message = "Tariffa oraria massima 200€")
    private BigDecimal hourlyRate;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "certification", columnDefinition = "TEXT")
    private String certification;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructors
    public TutorSubject() {}

    public TutorSubject(Tutor tutor, Subject subject, CompetenceLevel competenceLevel, BigDecimal hourlyRate) {
        this.tutor = tutor;
        this.subject = subject;
        this.competenceLevel = competenceLevel;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tutor getTutor() { return tutor; }
    public void setTutor(Tutor tutor) { this.tutor = tutor; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public CompetenceLevel getCompetenceLevel() { return competenceLevel; }
    public void setCompetenceLevel(CompetenceLevel competenceLevel) { this.competenceLevel = competenceLevel; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

    public Integer getYearsExperience() { return yearsExperience; }
    public void setYearsExperience(Integer yearsExperience) { this.yearsExperience = yearsExperience; }

    public String getCertification() { return certification; }
    public void setCertification(String certification) { this.certification = certification; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}