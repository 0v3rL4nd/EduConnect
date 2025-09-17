package com.piattaforme.educonnect.persistence.entity;

public enum CancelledBy {
    STUDENT("Studente"),
    TUTOR("Tutor"),
    SYSTEM("Sistema");

    private final String displayName;

    CancelledBy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
