package src.exceptions;

/**
 * Exception thrown when a transaction exceeds account limits.
 * <p>
 * This exception indicates that the transaction amount exceeds the allowed limit for the account.
 * </p>
 */
public class TransactionLimitException extends Exception {

    /**
     * Default constructor that initializes the exception with a default message.
     */
    public TransactionLimitException() {
        super("Transaction exceeds the allowed limit for this account");
    }

    /**
     * Constructor that initializes the exception with a custom message.
     *
     * @param message Custom message for the exception.
     */
    public TransactionLimitException(String message) {
        super(message);
    }

    /**
     * Constructor that initializes the exception with the transaction amount and limit.
     *
     * @param amount The amount attempted to be transacted.
     * @param limit The maximum allowed limit for the transaction.
     */
    public TransactionLimitException(double amount, double limit) {
        super(String.format("Transaction of ₱%.2f exceeds the limit of ₱%.2f", amount, limit));
    }
}