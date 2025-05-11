package src.services;

import src.exceptions.InvalidAccountException;
import src.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages bank accounts, including adding, retrieving, and saving accounts.
 */
public class AccountManager {
    private final List<Account> accounts;
    private static AccountManager instance;
    private final CSVUtility csvUtility;

    /**
     * Private constructor to initialize the AccountManager and load accounts from CSV.
     */
    private AccountManager() {
        this.csvUtility = new CSVUtility();
        this.accounts = csvUtility.loadAccounts(); // Load accounts on initialization
    }

    /**
     * Gets the singleton instance of AccountManager.
     *
     * @return the single instance of AccountManager
     */
    public static synchronized AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    /**
     * Adds a new account to the manager.
     *
     * @param account the account to be added
     */
    public void addAccount(Account account) {
        // Ensure that the account number is not incremented
        if (!accountNumberExists(account.getAccountNumber())) {
            accounts.add(account); // Add the new account to the list
        } else {
            System.err.println("Account number already exists: " + account.getAccountNumber());
        }
    }

    /**
     * Saves all accounts to a CSV file.
     */
    public void saveAccounts() {
        csvUtility.saveAccounts(accounts); // Save accounts when exiting
    }

    /**
     * Loads accounts from a CSV file, clearing existing accounts.
     */
    public void loadAccounts() {
        accounts.clear(); // Clear existing accounts
        accounts.addAll(csvUtility.loadAccounts()); // Load accounts from CSV
    }

    /**
     * Gets the latest account number of the last added account.
     *
     * @return the latest account number, or null if no accounts exist
     */
    public String getLatestAccountNumber() {
        if (accounts.isEmpty()) {
            return null; // or throw an exception if preferred
        }
        return accounts.get(accounts.size() - 1).getAccountNumber(); // Get the last account number
    }

    /**
     * Gets all accounts in the system.
     *
     * @return an unmodifiable list of all accounts
     */
    public List<Account> getAllAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    /**
     * Gets all active accounts in the system.
     *
     * @return a list of active accounts
     */
    public List<Account> getActiveAccounts() {
        return accounts.stream()
                .filter(Account::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets all closed accounts in the system.
     *
     * @return a list of closed accounts
     */
    public List<Account> getClosedAccounts() {
        return accounts.stream()
                .filter(account -> !account.isActive())
                .collect(Collectors.toList());
    }

    /**
     * Gets accounts by account holder name.
     *
     * @param name the account holder name to search for
     * @return a list of matching accounts
     */
    public List<Account> getAccountsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerCaseName = name.toLowerCase();
        return accounts.stream()
                .filter(account -> account.getAccountHolderName().toLowerCase().contains(lowerCaseName))
                .collect(Collectors.toList());
    }

    /**
     * Gets an account by account number.
     *
     * @param accountNumber the account number to search for
     * @return the matching account
     * @throws InvalidAccountException if no matching account is found
     */
    public Account getAccountByNumber(String accountNumber) throws InvalidAccountException {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new InvalidAccountException("Account number cannot be empty");
        }

        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new InvalidAccountException(accountNumber, "Account not found"));
    }

    /**
     * Applies monthly interest to all investment accounts.
     */
    public void applyMonthlyInterestToAllAccounts() {
        accounts.stream()
                .filter(account -> account.isActive() && account instanceof InvestmentAccount)
                .map(account -> (InvestmentAccount) account)
                .forEach(InvestmentAccount::applyInterest);
    }

    /**
     * Applies monthly interest to all credit card accounts.
     */
    public void applyMonthlyInterestToCreditCards() {
        accounts.stream()
                .filter(account -> account.isActive() && account instanceof CreditCardAccount)
                .map(account -> (CreditCardAccount) account)
                .forEach(CreditCardAccount::applyMonthlyInterest);
    }

    /**
     * Gets all accounts of a specific type.
     *
     * @param accountType the type of accounts to get
     * @return a list of accounts of the specified type
     */
    public List<Account> getAccountsByType(Class<? extends Account> accountType) {
        return accounts.stream()
                .filter(account -> accountType.isInstance(account))
                .collect(Collectors.toList());
    }

    /**
     * Resets monthly transaction counts for all checking accounts.
     */
    public void resetMonthlyTransactionCounts() {
        accounts.stream()
                .filter(account -> account.isActive() && account instanceof CheckingAccount)
                .map(account -> (CheckingAccount) account)
                .forEach(CheckingAccount::resetMonthlyTransactionCount);
    }

    /**
     * Resets daily withdrawal amounts for all bank accounts.
     */
    public void resetDailyWithdrawalAmounts() {
        accounts.stream()
                .filter(account -> account.isActive() && account instanceof BankAccount)
                .map(account -> (BankAccount) account)
                .forEach(BankAccount::resetDailyWithdrawalAmount);
    }

    /**
     * Checks if an account number exists in the system.
     *
     * @param accountNumber the account number to check
     * @return true if the account number exists, false otherwise
     */
    public boolean accountNumberExists(String accountNumber) {
        return accounts.stream().anyMatch(account -> account.getAccountNumber().equals(accountNumber));
    }
}