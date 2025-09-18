package com.piattaforme.educonnect.business.service;

import com.piattaforme.educonnect.business.dto.UserDTO;
import com.piattaforme.educonnect.business.service.NotificationService;
import com.piattaforme.educonnect.business.dto.UserRegistrationDTO;
import com.piattaforme.educonnect.persistence.entity.*;
import com.piattaforme.educonnect.persistence.repository.UserRepository;
import com.piattaforme.educonnect.persistence.repository.StudentRepository;
import com.piattaforme.educonnect.persistence.repository.TutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Transazioni dichiarative - default read-only
public class UserServiceImpl implements UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private StudentRepository studentRepository;

    @Inject
    private TutorRepository tutorRepository;

    @Inject
    private PasswordEncoder passwordEncoder;


    private NotificationService notificationService;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Student registerStudent(UserRegistrationDTO registrationDTO) {
        // Validazioni
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username già esistente");
        }

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email già registrata");
        }

        // Creazione studente
        Student student = new Student();
        student.setUsername(registrationDTO.getUsername());
        student.setEmail(registrationDTO.getEmail());
        student.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        student.setFirstName(registrationDTO.getFirstName());
        student.setLastName(registrationDTO.getLastName());
        student.setPhoneNumber(registrationDTO.getPhoneNumber());
        student.setSchoolName(registrationDTO.getSchoolName());
        student.setEducationLevel(registrationDTO.getEducationLevel());

        Student savedStudent = studentRepository.save(student);

        // Invio notifica di benvenuto
        notificationService.sendNotification(
                savedStudent.getId(),
                "Benvenuto in EduConnect!",
                "La tua registrazione è stata completata con successo. Inizia a cercare il tuo tutor ideale!",
                NotificationType.SYSTEM
        );

        return savedStudent;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public Tutor registerTutor(UserRegistrationDTO registrationDTO) {
        // Validazioni
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username già esistente");
        }

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email già registrata");
        }

        // Creazione tutor
        Tutor tutor = new Tutor();
        tutor.setUsername(registrationDTO.getUsername());
        tutor.setEmail(registrationDTO.getEmail());
        tutor.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        tutor.setFirstName(registrationDTO.getFirstName());
        tutor.setLastName(registrationDTO.getLastName());
        tutor.setPhoneNumber(registrationDTO.getPhoneNumber());
        tutor.setBio(registrationDTO.getBio());
        tutor.setEducationBackground(registrationDTO.getEducationBackground());
        tutor.setTeachingExperience(registrationDTO.getTeachingExperience());
        tutor.setHourlyRate(registrationDTO.getHourlyRate());
        tutor.setLocationCity(registrationDTO.getLocationCity());
        tutor.setLocationRegion(registrationDTO.getLocationRegion());
        tutor.setCanTeachOnline(registrationDTO.getCanTeachOnline());
        tutor.setCanTeachInPerson(registrationDTO.getCanTeachInPerson());

        Tutor savedTutor = tutorRepository.save(tutor);

        // Invio notifica di benvenuto
        notificationService.sendNotification(
                savedTutor.getId(),
                "Benvenuto in EduConnect!",
                "La tua registrazione come tutor è stata completata. Completa il tuo profilo aggiungendo le materie che insegni!",
                NotificationType.SYSTEM
        );

        return savedTutor;
    }

    @Override
    public Optional<User> authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findActiveUserByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateUserProfile(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        // Aggiornamento campi base
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfileImageUrl(userDTO.getProfileImageUrl());
        user.setDateOfBirth(userDTO.getDateOfBirth());

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        // Verifica password corrente
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Password corrente non corretta");
        }

        // Aggiornamento password
        user.setPassword(passwordEncoder.encode(newPassword));

        User savedUser = userRepository.save(user);

        // Notifica cambio password
        notificationService.sendNotification(
                userId,
                "Password modificata",
                "La tua password è stata modificata con successo.",
                NotificationType.SYSTEM
        );

        return savedUser;
    }

    @Override
    @Transactional(readOnly = false)
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void verifyUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        user.setIsVerified(true);
        userRepository.save(user);

        // Notifica verifica completata
        notificationService.sendNotification(
                userId,
                "Account verificato!",
                "Il tuo account è stato verificato con successo. Ora puoi accedere a tutte le funzionalità della piattaforma.",
                NotificationType.SYSTEM
        );
    }

    @Override
    public List<User> searchUsers(String searchTerm) {
        // Implementazione ricerca (da completare con query personalizzate)
        return userRepository.findAll(); // Placeholder
    }

    @Override
    public Page<User> searchUsers(String searchTerm, Pageable pageable) {
        // Implementazione ricerca con paginazione (da completare)
        return userRepository.findAll(pageable); // Placeholder
    }
}