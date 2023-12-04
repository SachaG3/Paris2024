package fr.normanbet.paris.p2024.exceptions;

public class DepositLimitExceededException extends RuntimeException {

    public DepositLimitExceededException() {
        super("Le montant du dépôt dépasse votre limite de dépôt.");
    }

    public DepositLimitExceededException(String message) {
        super(message);
    }
}
