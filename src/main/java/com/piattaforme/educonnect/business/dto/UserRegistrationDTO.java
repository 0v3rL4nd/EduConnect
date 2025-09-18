package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.EducationLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
public class UserRegistrationDTO {

    @NotBlank(message = "Username è obbligatorio")
    @Size(min = 3, max = 50, message = "Username deve essere tra 3 e 50 caratteri")
    private String username;

    @Email(message = "Email deve essere valida")
    @NotBlank(message = "Email è obbligatoria")
    private String email;

    @NotBlank(message = "Password è obbligatoria")
    @Size(min = 6, message = "Password deve essere almeno 6 caratteri")
    private String password;

    @NotBlank(message = "Conferma password è obbligatoria")
    private String confirmPassword;

    @NotBlank(message = "Nome è obbligatorio")
    private String firstName;

    @NotBlank(message = "Cognome è obbligatorio")
    private String lastName;

    private String phoneNumber;

    private String userType; // "STUDENT" o "TUTOR"

    // Campi specifici studente
    private String schoolName;
    private EducationLevel educationLevel;
    private Integer yearOfStudy;

    // Campi specifici tutor
    private String bio;
    private String educationBackground;
    private Integer teachingExperience;
    private BigDecimal hourlyRate;
    private String locationCity;
    private String locationRegion;
    private Boolean canTeachOnline;
    private Boolean canTeachInPerson;

    // Constructors
    public UserRegistrationDTO() {}

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    // Student fields
    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    public EducationLevel getEducationLevel() { return educationLevel; }
    public void setEducationLevel(EducationLevel educationLevel) { this.educationLevel = educationLevel; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    // Tutor fields
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getEducationBackground() { return educationBackground; }
    public void setEducationBackground(String educationBackground) { this.educationBackground = educationBackground; }

    public Integer getTeachingExperience() { return teachingExperience; }
    public void setTeachingExperience(Integer teachingExperience) { this.teachingExperience = teachingExperience; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getLocationCity() { return locationCity; }
    public void setLocationCity(String locationCity) { this.locationCity = locationCity; }

    public String getLocationRegion() { return locationRegion; }
    public void setLocationRegion(String locationRegion) { this.locationRegion = locationRegion; }

    public Boolean getCanTeachOnline() { return canTeachOnline; }
    public void setCanTeachOnline(Boolean canTeachOnline) { this.canTeachOnline = canTeachOnline; }

    public Boolean getCanTeachInPerson() { return canTeachInPerson; }
    public void setCanTeachInPerson(Boolean canTeachInPerson) { this.canTeachInPerson = canTeachInPerson; }
}