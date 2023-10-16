package fr.normanbet.paris.p2024.models.types;

public enum GenreType {
    FEMME("Femme"),
    HOMME("Homme");


    private final String name;

    private GenreType(String name) {
        this.name = name;
    }
}

