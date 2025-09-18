package com.piattaforme.educonnect.persistence.repository;


import com.piattaforme.educonnect.persistence.entity.Message;
import com.piattaforme.educonnect.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Conversazione tra due utenti
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.receiver = :user2) OR " +
            "(m.sender = :user2 AND m.receiver = :user1) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findConversationBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.receiver = :user2) OR " +
            "(m.sender = :user2 AND m.receiver = :user1) " +
            "ORDER BY m.sentAt ASC")
    Page<Message> findConversationBetweenUsers(@Param("user1") User user1, @Param("user2") User user2, Pageable pageable);

    // Messaggi ricevuti da un utente
    List<Message> findByReceiverOrderBySentAtDesc(User receiver);

    // Messaggi non letti
    @Query("SELECT m FROM Message m WHERE m.receiver = :receiver AND m.isRead = false ORDER BY m.sentAt DESC")
    List<Message> findUnreadMessages(@Param("receiver") User receiver);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver = :receiver AND m.isRead = false")
    Long countUnreadMessages(@Param("receiver") User receiver);

    // Ultime conversazioni per utente
    @Query("SELECT m FROM Message m WHERE m.sender = :user OR m.receiver = :user " +
            "ORDER BY m.sentAt DESC")
    List<Message> findRecentMessagesForUser(@Param("user") User user, Pageable pageable);
}
