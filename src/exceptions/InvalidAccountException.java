package src.exceptions;

/**
 * Exception thrown when an invalid account is accessed.
 * <p>
 * This exception indicates that the account details provided are either incorrect or the account does not exist.
 * </p>
 */
public class InvalidAccountException extends Exception {

    /**
     * Default constructor that initializes the exception with a default message.
     */
    public InvalidAccountException() {
        super("Invalid account details or account does not exist");
    }

    /**
     * Constructor that initializes the exception with a custom message.
     *
     * @param message Custom message for the exception.
     */
    public InvalidAccountException(String message) {
        super(message);
    }

    /**
     * Constructor that initializes the exception with account number and reason for invalidity.
     *
     * @param accountNumber The account number that is invalid.
     * @param reason The reason why the account is considered invalid.
     */
    public InvalidAccountException(String accountNumber, String reason) {
        super(String.format("Invalid account: %s - %s", accountNumber, reason));
    }
}