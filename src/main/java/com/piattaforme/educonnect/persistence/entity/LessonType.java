package com.piattaforme.educonnect.persistence.entity;

public enum LessonType {
    ONLINE("Online"),
    IN_PERSON("In Presenza"),
    HYBRID("Ibrida");

    private final String displayName;

    LessonType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
