package src.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a financial transaction in the banking system
 */
public class Transaction {
    private final LocalDateTime timestamp;
    private final String type;
    private final double amount;
    private final String description;
    private final double balanceAfterTransaction;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Creates a new transaction
     *
     * @param type the transaction type (deposit, withdrawal, etc.)
     * @param amount the amount of the transaction
     * @param description the description of the transaction
     * @param balanceAfterTransaction the account balance after this transaction
     */
    public Transaction(String type, double amount, String description, double balanceAfterTransaction) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }
    
    /**
     * Gets the timestamp of the transaction
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Gets the type of the transaction
     *
     * @return the transaction type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the amount of the transaction
     *
     * @return the transaction amount
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Gets the description of the transaction
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the account balance after this transaction
     *
     * @return the balance after transaction
     */
    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }
    
    /**
     * Returns a string representation of the transaction
     *
     * @return the transaction as a formatted string
     */
    @Override
    public String toString() {
        return String.format("[%s] %s: ₱%.2f - %s (Balance: ₱%.2f)", 
                timestamp.format(DATE_FORMATTER),
                type,
                amount,
                description,
                balanceAfterTransaction);
    }
}
