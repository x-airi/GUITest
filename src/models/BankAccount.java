package src.models;

import src.exceptions.InvalidAmountException;
import src.exceptions.TransactionLimitException;

/**
 * Represents a standard bank account
 */
public class BankAccount extends Account {
    private static final double DAILY_WITHDRAWAL_LIMIT = 1000.0;
    private static final double MIN_BALANCE = 100.0;
    private double dailyWithdrawalAmount;

    /**
     * Creates a new bank account with the specified details
     *
     * @param accountHolderName the name of the account holder
     * @param initialDeposit the initial deposit amount
     * @throws InvalidAmountException if the initial deposit is invalid
     */
    public BankAccount(String accountHolderName, double initialDeposit) throws InvalidAmountException {
        super(accountHolderName, initialDeposit);
        this.dailyWithdrawalAmount = 0.0;
    }

    /**
     * Gets the daily withdrawal limit
     *
     * @return the daily withdrawal limit
     */
    public double getDailyWithdrawalLimit() {
        return DAILY_WITHDRAWAL_LIMIT;
    }

    /**
     * Gets the minimum required balance
     *
     * @return the minimum balance
     */
    public double getMinimumBalance() {
        return MIN_BALANCE;
    }

    /**
     * Withdraws money from the account with daily limit checks
     *
     * @param amount the amount to withdraw
     * @throws Exception if the withdrawal cannot be processed
     */

    @Override
    public void logTransaction(String transactionType, double amount, String description) {
        // Call the superclass method to log the transaction
        super.logTransaction(transactionType, amount, description, balance);
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount > DAILY_WITHDRAWAL_LIMIT) {
            throw new TransactionLimitException(amount, DAILY_WITHDRAWAL_LIMIT);
        }

        if (dailyWithdrawalAmount + amount > DAILY_WITHDRAWAL_LIMIT) {
            double remainingLimit = DAILY_WITHDRAWAL_LIMIT - dailyWithdrawalAmount;
            throw new TransactionLimitException(
                    String.format("Daily withdrawal limit exceeded. Remaining limit: ₱%.2f", remainingLimit));
        }

        if (balance - amount < MIN_BALANCE) {
            throw new TransactionLimitException(
                    String.format("Withdrawal would drop balance below minimum (₱%.2f)", MIN_BALANCE));
        }

        super.withdraw(amount);
        dailyWithdrawalAmount += amount;
    }

    /**
     * Resets the daily withdrawal amount (would be called at the end of each day)
     */
    public void resetDailyWithdrawalAmount() {
        dailyWithdrawalAmount = 0.0;
    }

    @Override
    public String getAccountType() {
        return "Bank Account";
    }

    @Override
    public boolean verifyAccountDetails() {
        return super.verifyAccountDetails() && balance >= MIN_BALANCE;
    }
}
