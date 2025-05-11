package src.interfaces;

/**
 * Interface for transaction logging operations.
 * This interface defines methods for logging transactions and retrieving transaction history.
 */
public interface TransactionLoggable {

    /**
     * Logs a transaction for the account.
     *
     * @param transactionType the type of transaction (e.g., deposit, withdrawal).
     * @param amount the amount involved in the transaction.
     * @param description a description of the transaction.
     */
    void logTransaction(String transactionType, double amount, String description);

    /**
     * Gets the transaction history for the account.
     *
     * @return a string representation of the transaction history.
     */
    String getTransactionHistory();
}