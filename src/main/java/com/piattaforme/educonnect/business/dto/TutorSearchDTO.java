package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.LessonType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class TutorSearchDTO {
    private String subjectName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal minRating;
    private String location;
    private LessonType lessonType;
    private List<String> availableDays;
    private String timeSlot; // "morning", "afternoon", "evening"
    private Integer maxDistance; // in km
    private String sortBy; // "rating", "price", "experience", "distance"
    private String sortDirection; // "asc", "desc"

    // Constructors
    public TutorSearchDTO() {}

    // Getters and Setters
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public BigDecimal getMinRating() { return minRating; }
    public void setMinRating(BigDecimal minRating) { this.minRating = minRating; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LessonType getLessonType() { return lessonType; }
    public void setLessonType(LessonType lessonType) { this.lessonType = lessonType; }

    public List<String> getAvailableDays() { return availableDays; }
    public void setAvailableDays(List<String> availableDays) { this.availableDays = availableDays; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public Integer getMaxDistance() { return maxDistance; }
    public void setMaxDistance(Integer maxDistance) { this.maxDistance = maxDistance; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}