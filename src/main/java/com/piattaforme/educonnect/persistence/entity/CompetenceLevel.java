package com.piattaforme.educonnect.persistence.entity;

public enum CompetenceLevel {
    BEGINNER("Principiante"),
    INTERMEDIATE("Intermedio"),
    ADVANCED("Avanzato"),
    EXPERT("Esperto");

    private final String displayName;

    CompetenceLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}