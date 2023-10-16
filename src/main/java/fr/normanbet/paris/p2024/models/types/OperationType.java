package fr.normanbet.paris.p2024.models.types;

import lombok.Getter;

@Getter
public enum OperationType {
    DEPOSIT("Dépôt"),
    WITHDRAWAL("Retrait"),
    TRANSFER("Virement");

    private final String name;

    private OperationType(String name) {
        this.name = name;
    }

}

