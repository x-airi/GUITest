package src.models;

import src.exceptions.InvalidAmountException;
import src.exceptions.TransactionLimitException;

/**
 * Represents a checking account with transaction limits
 */
public class CheckingAccount extends Account {
    private static final double TRANSACTION_FEE = 1.5;
    private static final int FREE_TRANSACTIONS_PER_MONTH = 5;
    private int transactionsThisMonth;

    /**
     * Creates a new checking account with the specified details
     *
     * @param accountHolderName the name of the account holder
     * @param initialDeposit the initial deposit amount
     * @throws InvalidAmountException if the initial deposit is invalid
     */
    public CheckingAccount(String accountHolderName, double initialDeposit) throws InvalidAmountException {
        super(accountHolderName, initialDeposit);
        this.transactionsThisMonth = 0;
    }

    /**
     * Gets the transaction fee
     *
     * @return the transaction fee
     */
    public double getTransactionFee() {
        return TRANSACTION_FEE;
    }

    /**
     * Gets the number of free transactions per month
     *
     * @return the number of free transactions
     */
    public int getFreeTransactionsPerMonth() {
        return FREE_TRANSACTIONS_PER_MONTH;
    }

    /**
     * Gets the number of transactions performed this month
     *
     * @return the transaction count
     */
    public int getTransactionsThisMonth() {
        return transactionsThisMonth;
    }

    @Override
    public void deposit(double amount) throws Exception {
        super.deposit(amount);
        applyTransactionFee("Deposit");
    }

    @Override
    public void withdraw(double amount) throws Exception {
        super.withdraw(amount);
        applyTransactionFee("Withdrawal");
    }

    @Override
    public void transfer(Account destinationAccount, double amount) throws Exception {
        super.transfer(destinationAccount, amount);
        applyTransactionFee("Transfer");
    }

    /**
     * Applies a transaction fee if the free transaction limit has been exceeded
     */
    private void applyTransactionFee(String transactionType) throws Exception {
        transactionsThisMonth++;

        if (transactionsThisMonth > FREE_TRANSACTIONS_PER_MONTH) {
            if (balance < TRANSACTION_FEE) {
                throw new TransactionLimitException(
                        String.format("Insufficient funds to cover transaction fee of ₱%.2f", TRANSACTION_FEE));
            }

            balance -= TRANSACTION_FEE;
            logTransaction("Fee", TRANSACTION_FEE,
                    String.format("Transaction fee for %s (transaction #%d)",
                            transactionType, transactionsThisMonth), balance);
        }
    }

    /**
     * Resets the monthly transaction counter (would be called at the end of each month)
     */
    public void resetMonthlyTransactionCount() {
        transactionsThisMonth = 0;
    }

    @Override
    public void logTransaction(String transactionType, double amount, String description) {
        super.logTransaction(transactionType, amount, description, balance);
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }

    @Override
    public boolean verifyAccountDetails() {
        return super.verifyAccountDetails();
    }
}
