package src.services;

import src.exceptions.InvalidAccountException;
import src.exceptions.InvalidAmountException;
import src.models.Account;
import src.models.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides transaction history services for accounts.
 */
public class TransactionHistory {
    private final AccountManager accountManager;

    /**
     * Creates a new transaction history service.
     */
    public TransactionHistory() {
        this.accountManager = AccountManager.getInstance();
    }

    /**
     * Gets transaction history for an account.
     *
     * @param accountNumber the account number
     * @return the transaction history as a string
     * @throws InvalidAccountException if the account is not found
     */
    public String getTransactionHistory(String accountNumber) throws InvalidAccountException {
        Account account = accountManager.getAccountByNumber(accountNumber);
        return account.getTransactionHistory();
    }

    /**
     * Gets transactions for an account within a date range.
     *
     * @param accountNumber the account number
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a list of transactions within the date range
     * @throws InvalidAccountException if the account is not found
     */
    public List<Transaction> getTransactionsInDateRange(String accountNumber, LocalDate startDate, LocalDate endDate)
            throws InvalidAccountException {
        Account account = accountManager.getAccountByNumber(accountNumber);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return account.getTransactions().stream()
                .filter(transaction -> !transaction.getTimestamp().isBefore(startDateTime) &&
                        !transaction.getTimestamp().isAfter(endDateTime))
                .collect(Collectors.toList());
    }

    /**
     * Gets transactions of a specific type for an account.
     *
     * @param accountNumber the account number
     * @param transactionType the transaction type to filter by
     * @return a list of transactions of the specified type
     * @throws InvalidAccountException if the account is not found
     */
    public List<Transaction> getTransactionsByType(String accountNumber, String transactionType)
            throws InvalidAccountException {
        Account account = accountManager.getAccountByNumber(accountNumber);

        return account.getTransactions().stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase(transactionType))
                .collect(Collectors.toList());
    }

    /**
     * Gets the last N transactions for an account.
     *
     * @param accountNumber the account number
     * @param count the number of transactions to retrieve
     * @return a list of the most recent transactions
     * @throws InvalidAccountException if the account is not found
     */
    public List<Transaction> getRecentTransactions(String accountNumber, int count)
            throws InvalidAccountException {
        Account account = accountManager.getAccountByNumber(accountNumber);

        List<Transaction> transactions = account.getTransactions();
        int size = transactions.size();

        if (size <= count) {
            return transactions;
        } else {
            return transactions.subList(size - count, size);
        }
    }
}
