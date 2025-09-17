package com.piattaforme.educonnect.persistence.entity;

public enum MessageType {
    TEXT("Testo"),
    IMAGE("Immagine"),
    FILE("File"),
    SYSTEM("Sistema");

    private final String displayName;

    MessageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
