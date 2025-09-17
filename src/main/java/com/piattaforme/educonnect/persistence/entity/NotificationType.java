package com.piattaforme.educonnect.persistence.entity;

public enum NotificationType {
    BOOKING_CONFIRMED("Prenotazione Confermata"),
    BOOKING_CANCELLED("Prenotazione Cancellata"),
    LESSON_REMINDER("Promemoria Lezione"),
    PAYMENT_RECEIVED("Pagamento Ricevuto"),
    PAYMENT_FAILED("Pagamento Fallito"),
    NEW_MESSAGE("Nuovo Messaggio"),
    NEW_REVIEW("Nuova Recensione"),
    SYSTEM("Sistema");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}