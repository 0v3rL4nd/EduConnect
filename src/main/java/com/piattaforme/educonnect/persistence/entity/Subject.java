package com.piattaforme.educonnect.persistence.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Nome materia è obbligatorio")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private SubjectCategory category;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Relazioni
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TutorSubject> tutorSubjects = new HashSet<>();

    // Constructors
    public Subject() {}

    public Subject(String name, String description, SubjectCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public SubjectCategory getCategory() { return category; }
    public void setCategory(SubjectCategory category) { this.category = category; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Set<TutorSubject> getTutorSubjects() { return tutorSubjects; }
    public void setTutorSubjects(Set<TutorSubject> tutorSubjects) { this.tutorSubjects = tutorSubjects; }
}
