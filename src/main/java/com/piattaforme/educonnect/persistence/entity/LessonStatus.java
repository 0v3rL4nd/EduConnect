package com.piattaforme.educonnect.persistence.entity;

public enum LessonStatus {
    AVAILABLE("Disponibile"),
    BOOKED("Prenotata"),
    COMPLETED("Completata"),
    CANCELLED("Cancellata");

    private final String displayName;

    LessonStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}