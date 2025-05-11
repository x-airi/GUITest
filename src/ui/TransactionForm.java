package src.ui;

import src.exceptions.*;
import src.models.Account;
import src.services.AccountManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Form for performing transactions (deposit, withdraw, transfer).
 * This class provides a user interface for users to perform various banking transactions.
 */
public class TransactionForm extends JFrame {
    private JPanel mainPanel;
    private JTextField accountField;
    private JTextField amountField;
    private JButton processButton;
    private JButton cancelButton;
    private JLabel titleLabel;
    private JLabel accountLabel;
    private JLabel amountLabel;
    private JLabel descriptionLabel;
    private JTextField descriptionField;

    private final String transactionType;
    private final AccountManager accountManager;

    /**
     * Creates a new transaction form.
     *
     * @param transactionType the type of transaction (Deposit, Withdraw, Transfer)
     */
    public TransactionForm(String transactionType) {
        this.transactionType = transactionType;
        this.accountManager = AccountManager.getInstance();

        setupUI();
        setupActionListeners();
    }

    /**
     * Sets up the UI components.
     * This method initializes the components of the transaction form.
     */
    private void setupUI() {
        mainPanel = new JPanel(new GridBagLayout());
        titleLabel = new JLabel(transactionType + " Funds");
        accountLabel = new JLabel("Account Number:");
        amountLabel = new JLabel("Amount:");
        descriptionLabel = new JLabel("Description:");
        accountField = new JTextField();
        amountField = new JTextField();
        descriptionField = new JTextField();

        processButton = createStyledButton("Process " + transactionType);
        cancelButton = createStyledButton("Cancel");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        accountLabel.setFont(new Font("Arial", Font.BOLD, 35));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 35));
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 35));
        accountField.setFont(new Font("Arial", Font.BOLD, 35));
        amountField.setFont(new Font("Arial", Font.BOLD, 35));
        descriptionField.setFont(new Font("Arial", Font.BOLD, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        mainPanel.add(accountLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(accountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        mainPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.1;
        mainPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;
        mainPanel.add(processButton, gbc);

        gbc.gridy = 5;
        mainPanel.add(cancelButton, gbc);

        setContentPane(mainPanel);
        setTitle(transactionType + " Funds");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates a styled button with default properties.
     *
     * @param text the text for the button
     * @return the styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 35));
        button.setBackground(new Color(163, 41, 41));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    /**
     * Sets up the action listeners for the buttons.
     * This method defines the actions that occur when the process or cancel button is clicked.
     */
    private void setupActionListeners() {
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    processTransaction();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(TransactionForm.this,
                            "Error: " + ex.getMessage(), "Transaction Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AccountsMain();
            }
        });
    }

    /**
     * Processes the transaction based on the form data.
     *
     * @throws Exception if the transaction fails
     */
    private void processTransaction() throws Exception {
        String accountNumber = accountField.getText().trim();
        String amountText = amountField.getText().trim();
        String description = descriptionField.getText().trim();

        if (accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }

        if (amountText.isEmpty()) {
            throw new IllegalArgumentException("Amount is required");
        }

        if (description.isEmpty()) {
            description = transactionType + " transaction";
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a valid number");
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }

        Account account = accountManager.getAccountByNumber(accountNumber);

        switch (transactionType) {
            case "Deposit":
                account.deposit(amount);
                JOptionPane.showMessageDialog(this,
                        String.format("Successfully deposited ₱%.2f", amount),
                        "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);
                break;

            // Other transaction types (Withdraw, Transfer) can be handled similarly
            default:
                throw new IllegalArgumentException("Unknown transaction type");
        }

        account.logTransaction(transactionType, amount, description);

        dispose();
        new AccountsMain();
    }
}