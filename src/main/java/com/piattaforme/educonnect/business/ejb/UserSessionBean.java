package com.piattaforme.educonnect.business.ejb;


import com.piattaforme.educonnect.persistence.entity.User;
import com.piattaforme.educonnect.persistence.repository.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;


@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserSessionBean {

    @Inject
    private UserRepository userRepository;

    /**
     * Recupera tutti gli utenti - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Trova utente per ID - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Trova utente per username - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Crea nuovo utente - richiede transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User createUser(User user) {
        // Validazione username univoco
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username già esistente: " + user.getUsername());
        }

        // Validazione email univoca
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email già registrata: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    /**
     * Aggiorna utente esistente - richiede transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Utente non trovato con ID: " + user.getId());
        }

        return userRepository.save(user);
    }

    /**
     * Elimina utente - richiede transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Utente non trovato con ID: " + userId);
        }

        userRepository.deleteById(userId);
    }

    /**
     * Verifica disponibilità username - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Verifica disponibilità email - operazione read-only
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Attiva utente - richiede nuova transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        user.setIsActive(true);
        user.setIsVerified(true);
        userRepository.save(user);
    }
}
