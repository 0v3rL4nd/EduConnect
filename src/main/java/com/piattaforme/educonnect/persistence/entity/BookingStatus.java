package com.piattaforme.educonnect.persistence.entity;

public enum BookingStatus {
    PENDING("In Attesa"),
    CONFIRMED("Confermata"),
    COMPLETED("Completata"),
    CANCELLED("Cancellata");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
