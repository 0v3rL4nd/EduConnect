package com.piattaforme.educonnect.persistence.entity;

public enum PaymentStatus {
    PENDING("In Attesa"),
    PROCESSING("In Elaborazione"),
    COMPLETED("Completato"),
    FAILED("Fallito"),
    REFUNDED("Rimborsato");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}