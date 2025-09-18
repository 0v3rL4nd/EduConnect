package com.piattaforme.educonnect.business.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class TutorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String bio;
    private String educationBackground;
    private Integer teachingExperience;
    private BigDecimal hourlyRate;
    private BigDecimal rating;
    private Integer totalReviews;
    private Integer totalLessons;
    private Boolean isAvailable;
    private String locationCity;
    private String locationRegion;
    private Boolean canTeachOnline;
    private Boolean canTeachInPerson;
    private List<String> subjectsTeaching;
    private String profileImageUrl;

    // Constructors
    public TutorDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

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

    public List<String> getSubjectsTeaching() { return subjectsTeaching; }
    public void setSubjectsTeaching(List<String> subjectsTeaching) { this.subjectsTeaching = subjectsTeaching; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}