package com.piattaforme.educonnect.presentation.rest;

import com.piattaforme.educonnect.persistence.entity.Message;
import com.piattaforme.educonnect.persistence.entity.User;
import com.piattaforme.educonnect.persistence.repository.MessageRepository;
import com.piattaforme.educonnect.persistence.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatRestController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Ottieni conversazione tra due utenti
     */
    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable Long otherUserId,
                                                         HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = userRepository.findById(currentUserId).orElse(null);
        User otherUser = userRepository.findById(otherUserId).orElse(null);

        if (currentUser == null || otherUser == null) {
            return ResponseEntity.notFound().build();
        }

        List<Message> messages = messageRepository.findConversationBetweenUsers(currentUser, otherUser);
        return ResponseEntity.ok(messages);
    }

    /**
     * Invia nuovo messaggio
     */
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestParam Long receiverId,
                                               @RequestParam String content,
                                               HttpSession session) {
        Long senderId = (Long) session.getAttribute("userId");
        if (senderId == null) {
            return ResponseEntity.status(401).build();
        }

        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);

        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().build();
        }

        Message message = new Message(sender, receiver, content);
        message = messageRepository.save(message);

        // Invia messaggio real-time via WebSocket
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/messages",
                message
        );

        return ResponseEntity.ok(message);
    }

    /**
     * Ottieni messaggi non letti
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Message>> getUnreadMessages(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Message> unreadMessages = messageRepository.findUnreadMessages(user);
        return ResponseEntity.ok(unreadMessages);
    }

    /**
     * Segna messaggio come letto
     */
    @PostMapping("/{messageId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long messageId,
                                             HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            return ResponseEntity.notFound().build();
        }

        // Verifica che l'utente corrente sia il destinatario
        if (!message.getReceiver().getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        message.markAsRead();
        messageRepository.save(message);

        return ResponseEntity.ok("Messaggio segnato come letto");
    }

    /**
     * WebSocket endpoint per messaggi real-time
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public Message sendMessageWebSocket(Message message) {
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }
}