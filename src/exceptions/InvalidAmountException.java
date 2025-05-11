package src.exceptions;

/**
 * Exception thrown when an invalid amount is used in a transaction.
 * <p>
 * This exception indicates that the transaction amount provided is not acceptable (e.g., negative or zero).
 * </p>
 */
public class InvalidAmountException extends Exception {

    /**
     * Default constructor that initializes the exception with a default message.
     */
    public InvalidAmountException() {
        super("Invalid transaction amount");
    }

    /**
     * Constructor that initializes the exception with a custom message.
     *
     * @param message Custom message for the exception.
     */
    public InvalidAmountException(String message) {
        super(message);
    }

    /**
     * Constructor that initializes the exception with the invalid amount.
     *
     * @param amount The invalid amount that was attempted to be used.
     */
    public InvalidAmountException(double amount) {
        super(String.format("Invalid amount: ₱%.2f. Amount must be positive", amount));
    }
}