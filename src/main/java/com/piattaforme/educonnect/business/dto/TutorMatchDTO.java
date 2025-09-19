package com.piattaforme.educonnect.business.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TutorMatchDTO {
    private Long tutorId;
    private String tutorName;
    private String bio;
    private BigDecimal hourlyRate;
    private BigDecimal rating;
    private Integer totalReviews;
    private String location;
    private Double compatibilityScore; // 0.0 - 1.0
    private String matchReason;
    private Boolean canTeachOnline;
    private Boolean canTeachInPerson;
    private String profileImageUrl;
    private String subjectExperience;

    // Constructors
    public TutorMatchDTO() {}

    // Getters and Setters
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }

    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getCompatibilityScore() { return compatibilityScore; }
    public void setCompatibilityScore(Double compatibilityScore) { this.compatibilityScore = compatibilityScore; }

    public String getMatchReason() { return matchReason; }
    public void setMatchReason(String matchReason) { this.matchReason = matchReason; }

    public Boolean getCanTeachOnline() { return canTeachOnline; }
    public void setCanTeachOnline(Boolean canTeachOnline) { this.canTeachOnline = canTeachOnline; }

    public Boolean getCanTeachInPerson() { return canTeachInPerson; }
    public void setCanTeachInPerson(Boolean canTeachInPerson) { this.canTeachInPerson = canTeachInPerson; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getSubjectExperience() { return subjectExperience; }
    public void setSubjectExperience(String subjectExperience) { this.subjectExperience = subjectExperience; }
}
