package src.exceptions;

/**
 * Exception thrown when an account has insufficient funds for a transaction.
 * <p>
 * This exception indicates that the requested transaction cannot be completed due to lack of funds.
 * </p>
 */
public class InsufficientFundsException extends Exception {

    /**
     * Default constructor that initializes the exception with a default message.
     */
    public InsufficientFundsException() {
        super("Insufficient funds available for this transaction");
    }

    /**
     * Constructor that initializes the exception with a custom message.
     *
     * @param message Custom message for the exception.
     */
    public InsufficientFundsException(String message) {
        super(message);
    }

    /**
     * Constructor that initializes the exception with the requested amount and available balance.
     *
     * @param amount The amount requested for the transaction.
     * @param balance The available balance in the account.
     */
    public InsufficientFundsException(double amount, double balance) {
        super(String.format("Insufficient funds: Requested ₱%.2f but available balance is ₱%.2f", amount, balance));
    }
}