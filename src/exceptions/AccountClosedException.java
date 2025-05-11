package src.exceptions;

/**
 * Exception thrown when attempting operations on a closed account.
 * <p>
 * This exception indicates that the account is no longer active and cannot be used for transactions.
 * </p>
 */
public class AccountClosedException extends Exception {

    /**
     * Default constructor that initializes the exception with a default message.
     */
    public AccountClosedException() {
        super("This account has been closed and is no longer active");
    }

    /**
     * Constructor that initializes the exception with a custom message.
     *
     * @param message Custom message for the exception.
     */
    public AccountClosedException(String message) {
        super(message);
    }

    /**
     * Constructor that initializes the exception with account number and closure date.
     *
     * @param accountNumber The account number that was closed.
     * @param date The date when the account was closed.
     */
    public AccountClosedException(String accountNumber, String date) {
        super(String.format("Account %s was closed on %s and is no longer active", accountNumber, date));
    }
}