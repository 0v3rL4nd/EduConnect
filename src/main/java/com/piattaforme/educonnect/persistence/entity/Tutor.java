package com.piattaforme.educonnect.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tutors")
@DiscriminatorValue("TUTOR")
@PrimaryKeyJoinColumn(name = "user_id")
public class Tutor extends User {

    @Column(name = "tutor_bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "education_background", columnDefinition = "TEXT")
    private String educationBackground;

    @Column(name = "teaching_experience")
    private Integer teachingExperience; // anni di esperienza

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    @DecimalMin(value = "5.0", message = "Tariffa oraria minima 5€")
    @DecimalMax(value = "200.0", message = "Tariffa oraria massima 200€")
    private BigDecimal hourlyRate;

    @Column(name = "rating", precision = 3, scale = 2)
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "total_lessons")
    private Integer totalLessons = 0;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "location_city")
    private String locationCity;

    @Column(name = "location_region")
    private String locationRegion;

    @Column(name = "can_teach_online")
    private Boolean canTeachOnline = true;

    @Column(name = "can_teach_in_person")
    private Boolean canTeachInPerson = false;

    // Relazioni
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TutorSubject> tutorSubjects = new HashSet<>();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviewsReceived = new HashSet<>();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Availability> availabilities = new HashSet<>();

    // Constructors
    public Tutor() {
        super();
    }

    public Tutor(String username, String email, String password, String firstName, String lastName) {
        super(username, email, password, firstName, lastName);
    }

    // Business Methods
    public void updateRating() {
        if (totalReviews > 0) {
            BigDecimal sum = reviewsReceived.stream()
                    .map(Review::getRating)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            this.rating = sum.divide(new BigDecimal(totalReviews), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    // Getters and Setters
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getEducationBackground() { return educationBackground; }
    public void setEducationBackground(String educationBackground) { this.educationBackground = educationBackground; }

    public Integer getTeachingExperience() { return teachingExperience; }
    public void setTeachingExperience(Integer teachingExperience) { this.teachingExperience = teachingExperience; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public Integer getTotalLessons() { return totalLessons; }
    public void setTotalLessons(Integer totalLessons) { this.totalLessons = totalLessons; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public String getLocationCity() { return locationCity; }
    public void setLocationCity(String locationCity) { this.locationCity = locationCity; }

    public String getLocationRegion() { return locationRegion; }
    public void setLocationRegion(String locationRegion) { this.locationRegion = locationRegion; }

    public Boolean getCanTeachOnline() { return canTeachOnline; }
    public void setCanTeachOnline(Boolean canTeachOnline) { this.canTeachOnline = canTeachOnline; }

    public Boolean getCanTeachInPerson() { return canTeachInPerson; }
    public void setCanTeachInPerson(Boolean canTeachInPerson) { this.canTeachInPerson = canTeachInPerson; }

    public Set<TutorSubject> getTutorSubjects() { return tutorSubjects; }
    public void setTutorSubjects(Set<TutorSubject> tutorSubjects) { this.tutorSubjects = tutorSubjects; }

    public Set<Lesson> getLessons() { return lessons; }
    public void setLessons(Set<Lesson> lessons) { this.lessons = lessons; }

    public Set<Review> getReviewsReceived() { return reviewsReceived; }
    public void setReviewsReceived(Set<Review> reviewsReceived) { this.reviewsReceived = reviewsReceived; }

    public Set<Availability> getAvailabilities() { return availabilities; }
    public void setAvailabilities(Set<Availability> availabilities) { this.availabilities = availabilities; }
}