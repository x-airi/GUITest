package src.services;

import src.exceptions.InvalidAccountException;
import src.exceptions.InvalidAmountException;
import src.models.Account;
import src.models.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generates reports about accounts and transactions.
 */
public class ReportGenerator {
    private final AccountManager accountManager;
    private final TransactionHistory transactionHistory;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String ACTIVE_REPORTS_DIR = "Active Account Reports";
    private static final String CLOSED_REPORTS_DIR = "Closed Account Reports";
    private static final String TRANSACTION_SUMMARY_REPORTS_DIR = "Transaction Summary Reports";

    /**
     * Creates a new report generator.
     */
    public ReportGenerator() {
        this.accountManager = AccountManager.getInstance();
        this.transactionHistory = new TransactionHistory();

        // Create directories if they do not exist
        createDirectory(ACTIVE_REPORTS_DIR);
        createDirectory(CLOSED_REPORTS_DIR);
        createDirectory(TRANSACTION_SUMMARY_REPORTS_DIR);
    }

    /**
     * Creates a directory if it does not exist.
     *
     * @param dirName the name of the directory to create
     */
    private void createDirectory(String dirName) {
        Path path = Paths.get(dirName);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + dirName + " - " + e.getMessage());
            }
        }
    }

    /**
     * Generates a report of all active accounts.
     *
     * @return the filename of the generated report
     * @throws IOException if the report cannot be written
     */
    public String generateActiveAccountsReport() throws IOException {
        List<Account> activeAccounts = accountManager.getActiveAccounts();
        String filename = ACTIVE_REPORTS_DIR + "/active_accounts_report_" + getCurrentTimestamp() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("ACTIVE ACCOUNTS REPORT\n");
            writer.write("Generated on: " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
            writer.write("===========================================\n\n");

            writer.write(String.format("Total Active Accounts: %d\n\n", activeAccounts.size()));

            if (activeAccounts.isEmpty()) {
                writer.write("No active accounts found.\n");
            } else {
                for (Account account : activeAccounts) {
                    writer.write(account.getAccountDetails()); // This will include account number and holder name
                    writer.write("\n-----------------------------------------\n\n");
                }
            }
        }

        return filename;
    }

    /**
     * Generates a report of all closed accounts.
     *
     * @return the filename of the generated report
     * @throws IOException if the report cannot be written
     */
    public String generateClosedAccountsReport() throws IOException {
        List<Account> closedAccounts = accountManager.getClosedAccounts();
        String filename = CLOSED_REPORTS_DIR + "/closed_accounts_report_" + getCurrentTimestamp() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("CLOSED ACCOUNTS REPORT\n");
            writer.write("Generated on: " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
            writer.write("===========================================\n\n");

            writer.write(String.format("Total Closed Accounts: %d\n\n", closedAccounts.size()));

            if (closedAccounts.isEmpty()) {
                writer.write("No closed accounts found.\n");
            } else {
                for (Account account : closedAccounts) {
                    writer.write(account.getAccountDetails());
                    writer.write("\n-----------------------------------------\n\n");
                }
            }
        }

        return filename;
    }

    /**
     * Generates a transaction summary report for an account.
     *
     * @param accountNumber the account number
     * @return the filename of the generated report
     * @throws IOException if the report cannot be written
     * @throws InvalidAccountException if the account is not found
     */
    public String generateAccountTransactionSummary(String accountNumber)
            throws IOException, InvalidAccountException {
        Account account = accountManager.getAccountByNumber(accountNumber);
        String filename = TRANSACTION_SUMMARY_REPORTS_DIR + "/transaction_summary_" + accountNumber + "_" + getCurrentTimestamp() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("TRANSACTION SUMMARY REPORT\n");
            writer.write("Generated on: " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
            writer.write("===========================================\n\n");

            writer.write("Account Details:\n");
            writer.write(account.getAccountDetails());
            writer.write("\n-----------------------------------------\n\n");

            writer.write("Transaction History:\n");
            writer.write(account.getTransactionHistory());
        }

        return filename;
    }

    /**
     * Generates a report of all transactions within a date range for an account.
     *
     * @param accountNumber the account number
     * @param startDate the start date
     * @param endDate the end date
     * @return the filename of the generated report
     * @throws IOException if the report cannot be written
     * @throws InvalidAccountException if the account is not found
     */
    public String generateTransactionReportByDateRange(String accountNumber, LocalDate startDate, LocalDate endDate)
            throws IOException, InvalidAccountException {
        Account account = accountManager.getAccountByNumber(accountNumber);
        List<Transaction> transactions = transactionHistory.getTransactionsInDateRange(
                accountNumber, startDate, endDate);

        String filename = "transaction_report_" + accountNumber + "_" +
                startDate.format(DATE_FORMATTER) + "_to_" +
                endDate.format(DATE_FORMATTER) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("TRANSACTION REPORT BY DATE RANGE\n");
            writer.write("Generated on: " + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "\n");
            writer.write("===========================================\n\n");

            writer.write("Account Details:\n");
            writer.write(account.getAccountDetails());
            writer.write("\n-----------------------------------------\n\n");

            writer.write(String.format("Transactions from %s to %s:\n\n",
                    startDate.format(DATE_FORMATTER), endDate.format(DATE_FORMATTER)));

            if (transactions.isEmpty()) {
                writer.write("No transactions found in this date range.\n");
            } else {
                for (Transaction transaction : transactions) {
                    writer.write(transaction.toString() + "\n");
                }
            }
        }

        return filename;
    }

    /**
     * Gets the current timestamp as a string for filenames.
     *
     * @return the formatted timestamp
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}