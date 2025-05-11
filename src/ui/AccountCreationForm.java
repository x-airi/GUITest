package src.ui;

import src.exceptions.InvalidAmountException;
import src.models.*;
import src.services.AccountManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Form for creating new accounts.
 * This class provides a user interface for creating different types of accounts.
 */
public class AccountCreationForm extends JFrame {

    private JPanel mainPanel;
    private JTextField nameField;
    private JTextField amountField;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel titleLabel;
    private JLabel nameLabel;
    private JLabel amountLabel;
    private JLabel infoLabel;
    private final String accountType;
    private final AccountManager accountManager;

    /**
     * Creates a new account creation form.
     *
     * @param accountType the type of account to create
     */
    public AccountCreationForm(String accountType) {
        this.accountType = accountType;
        this.accountManager = AccountManager.getInstance();

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
        titleLabel = new JLabel("Create New " + accountType);
        nameLabel = new JLabel("Account Holder Name:");
        amountLabel = new JLabel(getLabelForAccountType());
        infoLabel = new JLabel(getInfoForAccountType());
        nameField = new JTextField(20);
        amountField = new JTextField(20);
        createButton = createStyledButton("Create Account");
        cancelButton = createStyledButton("Cancel");

        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 25));
        infoLabel.setFont(new Font("Arial", Font.BOLD, 25));
        nameField.setFont(new Font("Arial", Font.PLAIN, 25));
        amountField.setFont(new Font("Arial", Font.PLAIN, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(infoLabel, gbc);

        gbc.gridy = 2;
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(createButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(cancelButton, gbc);

        setContentPane(mainPanel);
        setTitle("Create " + accountType);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Gets the appropriate label for the amount field
     *
     * @return the label text
     */
    private String getLabelForAccountType() {
        switch (accountType) {
            case "Bank Account":
            case "Checking Account":
            case "Investment Account":
                return "Initial Deposit Amount:";
            case "Credit Card Account":
                return "Credit Limit:";
            default:
                return "Amount:";
        }
    }

    /**
     * Gets information text specific to the account type
     *
     * @return the information text
     */
    private String getInfoForAccountType() {
        return switch (accountType) {
            case "Bank Account" -> "Standard account with daily withdrawal limits and minimum balance requirement";
            case "Checking Account" ->
                    "Account for regular transactions with transaction fee after 5 transactions per month";
            case "Investment Account" -> "Account that earns interest on balances over ₱1,000";
            case "Credit Card Account" ->
                    "Credit card with revolving credit line and monthly interest on unpaid balances";
            default -> "";
        };
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
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createAccount();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AccountCreationForm.this,
                            "Error: " + ex.getMessage(), "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AccountTypes();
            }
        });
    }

    /**
     * Creates an account based on the form data
     *
     * @throws Exception if the account creation fails
     */
    private void createAccount() throws Exception {
        String name = nameField.getText().trim();
        String amountText = amountField.getText().trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Account holder name is required");
        }

        if (amountText.isEmpty()) {
            throw new IllegalArgumentException("Amount is required");
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

        Account newAccount = null;

        switch (accountType) {
            case "Bank Account":
                newAccount = new BankAccount(name, amount);
                break;
            case "Checking Account":
                newAccount = new CheckingAccount(name, amount);
                break;
            case "Investment Account":
                newAccount = new InvestmentAccount(name, amount);
                break;
            case "Credit Card Account":
                newAccount = new CreditCardAccount(name, amount);
                break;
            default:
                throw new IllegalArgumentException("Unknown account type");
        }

        AccountManager.getInstance().addAccount(newAccount);
        AccountManager.getInstance().saveAccounts();

        String accountNumber = newAccount.getAccountNumber();
        JOptionPane.showMessageDialog(this,
                "Account created successfully!\nYour account number is: " + accountNumber,
                "Account Created", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new AccountsMain();
    }
}