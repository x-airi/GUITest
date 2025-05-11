package src.models;

import src.exceptions.*;
import src.interfaces.AccountVerifiable;
import src.interfaces.TransactionLoggable;
import src.services.AccountManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract base class for all account types.
 * This class implements the AccountVerifiable and TransactionLoggable interfaces.
 */
public abstract class Account implements AccountVerifiable, TransactionLoggable {
    protected String accountNumber; // Unique account number
    protected String accountHolderName; // Name of the account holder
    protected double balance; // Current balance of the account
    protected LocalDate openingDate; // Date when the account was opened
    protected LocalDate closingDate; // Date when the account was closed (if applicable)
    protected boolean isActive; // Status of the account (active or closed)
    protected List<Transaction> transactions; // List of transactions associated with the account

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Random random = new Random(); // Random number generator

    // Static counters for generating unique account numbers
    private static final int BANK_ACCOUNT_PREFIX = 100000000; // Prefix for Bank Accounts
    private static final int INVESTMENT_ACCOUNT_PREFIX = 200000000; // Prefix for Investment Accounts
    private static final int CHECKING_ACCOUNT_PREFIX = 300000000; // Prefix for Checking Accounts
    private static final int CREDIT_CARD_ACCOUNT_PREFIX = 400000000; // Prefix for Credit Card Accounts

    /**
     * Creates a new account with the specified details.
     *
     * @param accountHolderName the name of the account holder
     * @param initialDeposit the initial deposit amount
     * @throws InvalidAmountException if the initial deposit is invalid
     */
    public Account(String accountHolderName, double initialDeposit) throws InvalidAmountException {
        if (initialDeposit < 0) {
            throw new InvalidAmountException(initialDeposit);
        }

        this.accountHolderName = accountHolderName; // Set account holder name
        this.balance = initialDeposit; // Set initial balance
        this.openingDate = LocalDate.now(); // Set opening date to current date
        this.isActive = true; // Set account status to active
        this.transactions = new ArrayList<>(); // Initialize transaction list

        // Generate and set the unique account number
        this.accountNumber = generateAccountNumber();

        // Log initial deposit transaction if applicable
        if (initialDeposit > 0) {
            logTransaction("Initial Deposit", initialDeposit, "Account opening deposit", balance);
        }
    }

    /**
     * Generates a unique account number based on the account type.
     *
     * @return a unique account number
     */
    protected String generateAccountNumber() {
        int accountNumber;
        switch (getAccountType()) {
            case "Bank Account":
                accountNumber = BANK_ACCOUNT_PREFIX + random.nextInt(1000000); // Random number in the range of 100000000 to 1000000000
                break;
            case "Investment Account":
                accountNumber = INVESTMENT_ACCOUNT_PREFIX + random.nextInt(1000000); // Random number in the range of 200000000 to 2000000000
                break;
            case "Checking Account":
                accountNumber = CHECKING_ACCOUNT_PREFIX + random.nextInt(1000000); // Random number in the range of 300000000 to 3000000000
                break;
            case "Credit Card Account":
                accountNumber = CREDIT_CARD_ACCOUNT_PREFIX + random.nextInt(1000000); // Random number in the range of 400000000 to 4000000000
                break;
            default:
                throw new IllegalArgumentException("Unknown account type");
        }
        return String.valueOf(accountNumber);
    }

    // Setter for isActive
    public void setActive(boolean isActive) {
        this.isActive = isActive; // Set the active status of the account
    }

    // Setter for openingDate
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate; // Set the opening date of the account
    }

    // Setter for closingDate
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate; // Set the closing date of the account
    }

    // Getters for account properties
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Deposits money into the account.
     *
     * @param amount the amount to deposit
     * @throws AccountClosedException if the account is closed
     * @throws InvalidAmountException if the deposit amount is invalid
     * @throws Exception for other exceptions
     */
    public void deposit(double amount) throws AccountClosedException, InvalidAmountException, Exception {
        if (!isActive) {
            throw new AccountClosedException();
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }

        balance += amount; // Update balance
        logTransaction("Deposit", amount, "Cash deposit", balance); // Log transaction
    }

    /**
     * Withdraws money from the account.
     *
     * @param amount the amount to withdraw
     * @throws AccountClosedException if the account is closed
     * @throws InvalidAmountException if the withdrawal amount is invalid
     * @throws InsufficientFundsException if there are insufficient funds
     * @throws Exception for other exceptions
     */
    public void withdraw(double amount) throws AccountClosedException, InvalidAmountException, InsufficientFundsException, Exception {
        if (!isActive) {
            throw new AccountClosedException();
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }

        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }

        balance -= amount; // Update balance
        logTransaction("Withdrawal", amount, "Cash withdrawal", balance); // Log transaction
    }

    /**
     * Transfers money to another account.
     *
     * @param destinationAccount the account to transfer money to
     * @param amount the amount to transfer
     * @throws AccountClosedException if the account is closed
     * @throws InvalidAmountException if the transfer amount is invalid
     * @throws InsufficientFundsException if there are insufficient funds
     * @throws InvalidAccountException if the destination account is invalid
     * @throws Exception for other exceptions
     */
    public void transfer(Account destinationAccount, double amount) throws AccountClosedException, InvalidAmountException, InsufficientFundsException, InvalidAccountException, Exception {
        if (!isActive) {
            throw new AccountClosedException();
        }

        if (destinationAccount == null) {
            throw new InvalidAccountException("Destination account does not exist");
        }

        if (!destinationAccount.isActive()) {
            throw new AccountClosedException("Destination account is closed");
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive");
        }

        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }

        balance -= amount; // Update balance
        logTransaction("Transfer Out", amount, "Transfer to account " + destinationAccount.getAccountNumber(), balance); // Log transaction

        destinationAccount.receiveTransfer(this, amount); // Process transfer to destination account
    }

    /**
     * Receives a transfer from another account.
     *
     * @param sourceAccount the account transferring money
     * @param amount the amount being transferred
     */
    protected void receiveTransfer(Account sourceAccount, double amount) {
        balance += amount; // Update balance
        logTransaction("Transfer In", amount, "Transfer from account " + sourceAccount.getAccountNumber(), balance); // Log transaction
    }

    /**
     * Closes the account.
     *
     * @throws AccountClosedException if the account is already closed
     * @throws InsufficientFundsException if the account has a negative balance
     */
    public void closeAccount() throws AccountClosedException, InsufficientFundsException {
        if (!isActive) {
            throw new AccountClosedException("Account is already closed");
        }

        if (balance < 0) {
            throw new InsufficientFundsException("Cannot close account with negative balance");
        }

        isActive = false; // Set account status to closed
        closingDate = LocalDate.now(); // Set closing date to current date
        logTransaction("Account Closed", 0, "Account closed with final balance of ₱" + balance, balance); // Log transaction

        // Save accounts after closing
        AccountManager.getInstance().saveAccounts();
    }

    /**
     * Reopens the account.
     *
     * @throws AccountClosedException if the account is already active
     */
    public void reopenAccount() throws AccountClosedException {
        if (isActive) {
            throw new AccountClosedException("Account is already active");
        }

        isActive = true; // Set account status to active
        closingDate = null; // Clear the closing date
        logTransaction("Account Reopened", 0, "Account reopened", balance); // Log transaction

        // Save accounts after reopening
        AccountManager.getInstance().saveAccounts();
    }

    /**
     * Logs a transaction.
     *
     * @param transactionType the type of transaction
     * @param amount the amount of the transaction
     * @param description the description of the transaction
     * @param balanceAfter the balance after the transaction
     */
    protected void logTransaction(String transactionType, double amount, String description, double balanceAfter) {
        Transaction transaction = new Transaction(transactionType, amount, description, balanceAfter);
        transactions.add(transaction); // Add transaction to the list
    }

    /**
     * Gets account details as a string.
     *
     * @return the account details
     */
    public String getAccountDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Account Number: ").append(accountNumber).append("\n");
        details.append("Account Holder: ").append(accountHolderName).append("\n");
        details.append("Account Type: ").append(getAccountType()).append("\n");
        details.append("Balance: ₱").append(String.format("%.2f", balance)).append("\n");
        details.append("Opening Date: ").append(openingDate.format(DATE_FORMATTER)).append("\n");
        details.append("Status: ").append(isActive ? "Active" : "Closed").append("\n");

        if (!isActive && closingDate != null) {
            details.append("Closing Date: ").append(closingDate.format(DATE_FORMATTER)).append("\n");
        }

        return details.toString(); // Return account details as a string
    }

    /**
     * Abstract method to get the account type.
     *
     * @return the account type
     */
    public abstract String getAccountType();

    @Override
    public boolean verifyAccountDetails() {
        // Base verification logic - can be overridden by subclasses
        return accountNumber != null && !accountNumber.isEmpty() &&
                accountHolderName != null && !accountHolderName.isEmpty() &&
                openingDate != null;
    }

    @Override
    public String getTransactionHistory() {
        StringBuilder history = new StringBuilder();
        history.append("Transaction History for Account ").append(accountNumber).append("\n");
        history.append("----------------------------------------\n");

        if (transactions.isEmpty()) {
            history.append("No transactions found\n");
        } else {
            for (Transaction transaction : transactions) {
                history.append(transaction.toString()).append("\n");
            }
        }

        return history.toString(); // Return transaction history as a string
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}