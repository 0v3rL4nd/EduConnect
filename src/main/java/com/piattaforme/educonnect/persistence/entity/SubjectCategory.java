package com.piattaforme.educonnect.persistence.entity;

public enum SubjectCategory {
    MATHEMATICS("Matematica"),
    SCIENCES("Scienze"),
    LANGUAGES("Lingue"),
    HUMANITIES("Materie Umanistiche"),
    COMPUTER_SCIENCE("Informatica"),
    ARTS("Arte"),
    MUSIC("Musica"),
    SPORTS("Sport"),
    OTHER("Altro");

    private final String displayName;

    SubjectCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
