package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.LessonType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MatchingCriteriaDTO {
    private String subjectName;
    private BigDecimal maxPrice;
    private BigDecimal minRating;
    private String preferredLocation;
    private Integer maxDistanceKm;
    private LessonType preferredLessonType;
    private List<String> preferredDays;
    private List<String> preferredTimeSlots;
    private String learningStyle; // "visual", "auditory", "kinesthetic"
    private String experienceLevel; // "beginner", "intermediate", "advanced"

    // Constructors
    public MatchingCriteriaDTO() {}

    // Getters and Setters
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public BigDecimal getMinRating() { return minRating; }
    public void setMinRating(BigDecimal minRating) { this.minRating = minRating; }

    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }

    public Integer getMaxDistanceKm() { return maxDistanceKm; }
    public void setMaxDistanceKm(Integer maxDistanceKm) { this.maxDistanceKm = maxDistanceKm; }

    public LessonType getPreferredLessonType() { return preferredLessonType; }
    public void setPreferredLessonType(LessonType preferredLessonType) { this.preferredLessonType = preferredLessonType; }

    public List<String> getPreferredDays() { return preferredDays; }
    public void setPreferredDays(List<String> preferredDays) { this.preferredDays = preferredDays; }

    public List<String> getPreferredTimeSlots() { return preferredTimeSlots; }
    public void setPreferredTimeSlots(List<String> preferredTimeSlots) { this.preferredTimeSlots = preferredTimeSlots; }

    public String getLearningStyle() { return learningStyle; }
    public void setLearningStyle(String learningStyle) { this.learningStyle = learningStyle; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }
}