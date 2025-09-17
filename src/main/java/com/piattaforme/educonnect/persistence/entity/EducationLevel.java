package com.piattaforme.educonnect.persistence.entity;

public enum EducationLevel {
    ELEMENTARY("Scuola Elementare"),
    MIDDLE_SCHOOL("Scuola Media"),
    HIGH_SCHOOL("Scuola Superiore"),
    BACHELOR("Laurea Triennale"),
    MASTER("Laurea Magistrale"),
    PHD("Dottorato"),
    OTHER("Altro");

    private final String displayName;

    EducationLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}