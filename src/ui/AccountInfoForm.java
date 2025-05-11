package src.ui;

import src.exceptions.InvalidAccountException;
import src.models.Account;
import src.services.AccountManager;
import src.services.ReportGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Form for viewing account information and generating reports
 */
public class AccountInfoForm extends JFrame {
    private JPanel mainPanel;
    private JTextField accountField;
    private JTextArea detailsArea;
    private JButton viewDetailsButton;
    private JButton viewTransactionsButton;
    private JButton generateReportButton;
    private JButton backButton;
    private JButton allActiveAccountsButton;
    private JButton allClosedAccountsButton;
    private JLabel accountLabel;
    private JLabel infoLabel;

    private final AccountManager accountManager;
    private final ReportGenerator reportGenerator;

    /**
     * Creates a new account information form
     */
    public AccountInfoForm() {
        this.accountManager = AccountManager.getInstance();
        this.reportGenerator = new ReportGenerator();

        setupUI();
        setupActionListeners();
    }

    /**
     * Sets up the UI components
     */
    private void setupUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        mainPanel = new JPanel(new GridBagLayout());
        accountLabel = new JLabel("Account Number:");
        infoLabel = new JLabel("Enter account number to view details");
        accountField = new JTextField(20);
        detailsArea = new JTextArea(15, 40);
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);

        viewDetailsButton = createStyledButton("View Details");
        viewTransactionsButton = createStyledButton("View Transactions");
        generateReportButton = createStyledButton("Generate Report");
        backButton = createStyledButton("Back");
        allActiveAccountsButton = createStyledButton("Active Accounts Report");
        allClosedAccountsButton = createStyledButton("Closed Accounts Report");

        accountLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        accountLabel.setFont(new Font("Arial", Font.BOLD, 35));
        infoLabel.setFont(new Font("Arial", Font.BOLD, 25));
        accountField.setFont(new Font("Arial", Font.PLAIN, 25));
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(infoLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(accountLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(accountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(scrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3;
        mainPanel.add(viewDetailsButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(viewTransactionsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(generateReportButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(backButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(allActiveAccountsButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(allClosedAccountsButton, gbc);

        setContentPane(mainPanel);
        setTitle("Account Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates a styled button with default properties
     *
     * @param text the text for the button
     * @return the styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setBackground(new Color(163, 41, 41));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    /**
     * Sets up the action listeners for the buttons
     */
    private void setupActionListeners() {
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String accountNumber = accountField.getText().trim();
                    if (accountNumber.isEmpty()) {
                        throw new IllegalArgumentException("Account number is required");
                    }

                    Account account = accountManager.getAccountByNumber(accountNumber);
                    detailsArea.setText(account.getAccountDetails());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Error: " + ex.getMessage(), "View Details Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewTransactionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String accountNumber = accountField.getText().trim();
                    if (accountNumber.isEmpty()) {
                        throw new IllegalArgumentException("Account number is required");
                    }

                    Account account = accountManager.getAccountByNumber(accountNumber);
                    detailsArea.setText(account.getTransactionHistory());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Error: " + ex.getMessage(), "View Transactions Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String accountNumber = accountField.getText().trim();
                    if (accountNumber.isEmpty()) {
                        throw new IllegalArgumentException("Account number is required");
                    }

                    String startDateStr = JOptionPane.showInputDialog(AccountInfoForm.this,
                            "Enter start date (YYYY-MM-DD) or leave blank for all transactions:",
                            "Date Range", JOptionPane.QUESTION_MESSAGE);

                    String endDateStr = JOptionPane.showInputDialog(AccountInfoForm.this,
                            "Enter end date (YYYY-MM-DD) or leave blank for today:",
                            "Date Range", JOptionPane.QUESTION_MESSAGE);

                    String reportPath;
                    if (startDateStr != null && !startDateStr.trim().isEmpty() &&
                            endDateStr != null && !endDateStr.trim().isEmpty()) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate startDate = LocalDate.parse(startDateStr.trim(), formatter);
                            LocalDate endDate = LocalDate.parse(endDateStr.trim(), formatter);

                            reportPath = reportGenerator.generateTransactionReportByDateRange(
                                    accountNumber, startDate, endDate);
                        } catch (DateTimeParseException ex) {
                            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
                        }
                    } else {
                        reportPath = reportGenerator.generateAccountTransactionSummary(accountNumber);
                    }

                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Report generated successfully: " + reportPath,
                            "Report Generated", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Error: " + ex.getMessage(), "Generate Report Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AccountsMain();
            }
        });

        allActiveAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String reportPath = reportGenerator.generateActiveAccountsReport();
                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Active accounts report generated: " + reportPath,
                            "Report Generated", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Error generating report: " + ex.getMessage(),
                            "Report Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        allClosedAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String reportPath = reportGenerator.generateClosedAccountsReport();
                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Closed accounts report generated: " + reportPath,
                            "Report Generated", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(AccountInfoForm.this,
                            "Error generating report: " + ex.getMessage(),
                            "Report Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}