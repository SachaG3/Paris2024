package fr.normanbet.paris.p2024.models.types;

public enum DisciplineType {
    FEMMES("Femmes"),
    HOMMES("Hommes"),
    MIXTE("Mixte");

    private final String name;

    private DisciplineType(String name) {
        this.name = name;
    }
}
