package fr.normanbet.paris.p2024.exceptions;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("Fonds insuffisants pour le retrait.");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
