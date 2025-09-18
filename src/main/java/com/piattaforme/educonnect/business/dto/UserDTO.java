package com.piattaforme.educonnect.business.dto;

import com.piattaforme.educonnect.persistence.entity.EducationLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime dateOfBirth;
    private Boolean isActive;
    private Boolean isVerified;

    // Constructors
    public UserDTO() {}

}