package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.business.dto.UserDTO;
import com.piattaforme.educonnect.business.dto.UserRegistrationDTO;
import com.piattaforme.educonnect.persistence.entity.Student;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import com.piattaforme.educonnect.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Operazioni CRUD base
    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(Long id);

    // Registrazione utenti
    Student registerStudent(UserRegistrationDTO registrationDTO);

    Tutor registerTutor(UserRegistrationDTO registrationDTO);

    // Autenticazione
    Optional<User> authenticateUser(String username, String password);

    boolean isUsernameAvailable(String username);

    boolean isEmailAvailable(String email);

    // Gestione profilo
    User updateUserProfile(Long userId, UserDTO userDTO);

    User changePassword(Long userId, String oldPassword, String newPassword);

    void activateUser(Long userId);

    void deactivateUser(Long userId);

    void verifyUser(Long userId);

    // Ricerca utenti
    List<User> searchUsers(String searchTerm);

    Page<User> searchUsers(String searchTerm, Pageable pageable);
}