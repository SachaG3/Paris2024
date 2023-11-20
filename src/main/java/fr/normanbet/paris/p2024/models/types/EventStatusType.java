package fr.normanbet.paris.p2024.models.types;

public enum EventStatusType {
    TERMINATED("Terminé"),
    NOT_STARTED("Non commencé"),
    STARTED("Commencé");


    private final String name;

    private EventStatusType(String name) {
        this.name = name;
    }
}
