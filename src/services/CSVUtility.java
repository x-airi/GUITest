package src.services;

import src.exceptions.InvalidAmountException;
import src.models.Account;
import src.models.BankAccount;
import src.models.CheckingAccount;
import src.models.CreditCardAccount;
import src.models.InvestmentAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Utility class for handling CSV file operations related to accounts.
 */
public class CSVUtility {
    private static final String CSV_FILE = "src/accounts.csv"; // Updated file path

    /**
     * Saves accounts to a CSV file.
     *
     * @param accounts the list of accounts to save
     */
    public void saveAccounts(List<Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            writer.write("Account Number,Account Holder Name,Balance,Account Type,Is Active,Opening Date,Closing Date\n");
            for (Account account : accounts) {
                writer.write(String.format("%s,%s,%.2f,%s,%b,%s,%s\n",
                        account.getAccountNumber(),
                        account.getAccountHolderName(),
                        account.getBalance(),
                        account.getAccountType(),
                        account.isActive(), // This will save the status of the account
                        account.getOpeningDate(),
                        account.getClosingDate() != null ? account.getClosingDate() : ""));
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    /**
     * Loads accounts from a CSV file.
     *
     * @return a list of accounts loaded from the CSV file
     */
    public List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String accountNumber = data[0];
                String accountHolderName = data[1];
                double balance = Double.parseDouble(data[2]);
                String accountType = data[3];
                boolean isActive = Boolean.parseBoolean(data[4]);
                String openingDate = data[5];
                String closingDate = data.length > 6 ? data[6] : null;

                // Create account based on type only if account number is not null
                if (accountNumber != null && !accountNumber.equals("null")) {
                    Account account = createAccount(accountType, accountHolderName, balance);
                    if (account != null) {
                        account.setActive(isActive); // This will now work without errors
                        account.setOpeningDate(LocalDate.parse(openingDate));
                        if (closingDate != null && !closingDate.isEmpty()) {
                            account.setClosingDate(LocalDate.parse(closingDate));
                        }
                        accounts.add(account);
                    } else {
                        System.err.println("Invalid account type for account: " + accountNumber);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Creates an account based on the specified type.
     *
     * @param accountType the type of account to create
     * @param accountHolderName the name of the account holder
     * @param initialDeposit the initial deposit amount
     * @return the created Account object, or null if the account type is invalid
     */
    private Account createAccount(String accountType, String accountHolderName, double initialDeposit) {
        try {
            switch (accountType) {
                case "Bank Account":
                    return new BankAccount(accountHolderName, initialDeposit);
                case "Checking Account":
                    return new CheckingAccount(accountHolderName, initialDeposit);
                case "Investment Account":
                    return new InvestmentAccount(accountHolderName, initialDeposit);
                case "Credit Card Account":
                    return new CreditCardAccount(accountHolderName, initialDeposit);
                default:
                    System.err.println("Unknown account type: " + accountType);
                    return null; // Return null for invalid account type
            }
        } catch (InvalidAmountException e) {
            System.err.println("Invalid initial deposit for account holder: " + accountHolderName);
            return null; // Return null if account creation fails
        }
    }
}