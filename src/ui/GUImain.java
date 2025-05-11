package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main GUI class for the banking application.
 * This class initializes the main interface for the banking application, allowing users to perform various banking operations.
 */
public class GUImain extends JFrame implements ActionListener {
    private JButton createAccountButton;
    private JButton balanceInquiryButton;
    private JButton depositButton;
    private JButton accountInformationButton;
    private JButton withdrawButton;
    private JButton moneyTransferButton;
    private JButton closeAccountButton;
    private JButton reopenAccountButton;
    private JButton exitButton;
    private JPanel mainPanel;

    /**
     * Creates the main GUI.
     * Initializes the GUI components and sets up action listeners for the buttons.
     */
    public GUImain() {
        setupUI();
        setupActionListeners();
    }

    /**
     * Sets up the UI components.
     * This method creates and arranges the main interface components.
     */
    private void setupUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 250, 255));

        Font buttonFont = new Font("Arial", Font.BOLD, 30);

        createAccountButton = createStyledButton("Create Account", buttonFont);
        balanceInquiryButton = createStyledButton("Balance Inquiry", buttonFont);
        depositButton = createStyledButton("Deposit", buttonFont);
        withdrawButton = createStyledButton("Withdraw", buttonFont);
        moneyTransferButton = createStyledButton("Money Transfer", buttonFont);
        accountInformationButton = createStyledButton("Account Information", buttonFont);
        closeAccountButton = createStyledButton("Close Account", buttonFont);
        reopenAccountButton = createStyledButton("Reopen Account", buttonFont);
        exitButton = createStyledButton("Exit", buttonFont);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("SABOG FINANCE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(createAccountButton, gbc);

        gbc.gridy = 2;
        mainPanel.add(depositButton, gbc);

        gbc.gridy = 3;
        mainPanel.add(moneyTransferButton, gbc);

        gbc.gridy = 4;
        mainPanel.add(closeAccountButton, gbc);

        gbc.gridy = 4;
        mainPanel.add(reopenAccountButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(balanceInquiryButton, gbc);

        gbc.gridy = 2;
        mainPanel.add(withdrawButton, gbc);

        gbc.gridy = 3;
        mainPanel.add(accountInformationButton, gbc);

        gbc.gridy = 4;
        mainPanel.add(exitButton, gbc);

        setContentPane(mainPanel);
        setTitle("SABOG FINANCE");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates a styled button with default properties.
     *
     * @param text the text for the button
     * @param font the font for the button
     * @return the styled JButton
     */
    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(163, 41, 41));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    /**
     * Sets up the action listeners for the buttons.
     * This method defines the actions that occur when each button is clicked.
     */
    private void setupActionListeners() {
        createAccountButton.addActionListener(e -> {
            dispose();
            new AccountTypes();
        });

        balanceInquiryButton.addActionListener(e -> {
            String accountNumber = JOptionPane.showInputDialog(this,
                    "Enter account number:", "Balance Inquiry", JOptionPane.QUESTION_MESSAGE);

            if (accountNumber != null && !accountNumber.trim().isEmpty()) {
                try {
                    double balance = src.services.AccountManager.getInstance()
                            .getAccountByNumber(accountNumber).getBalance();
                    JOptionPane.showMessageDialog(this,
                            String.format("Current balance: ₱%.2f", balance),
                            "Balance Inquiry", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(), "Balance Inquiry Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        depositButton.addActionListener(e -> {
            dispose();
            new TransactionForm("Deposit");
        });

        withdrawButton.addActionListener(e -> {
            dispose();
            new TransactionForm("Withdraw");
        });

        moneyTransferButton.addActionListener(e -> {
            dispose();
            new TransactionForm("Transfer");
        });

        accountInformationButton.addActionListener(e -> {
            dispose();
            new AccountInfoForm();
        });

        closeAccountButton.addActionListener(e -> {
            String accountNumber = JOptionPane.showInputDialog(this,
                    "Enter account number to close:", "Close Account", JOptionPane.QUESTION_MESSAGE);

            if (accountNumber != null && !accountNumber.trim().isEmpty()) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to close this account?\nThis action cannot be undone.",
                            "Confirm Account Closure", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        src.services.AccountManager.getInstance()
                                .getAccountByNumber(accountNumber).closeAccount();
                        JOptionPane.showMessageDialog(this,
                                "Account closed successfully.", "Account Closed", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(), "Close Account Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        reopenAccountButton.addActionListener(e -> {
            String accountNumber = JOptionPane.showInputDialog(this,
                    "Enter account number to reopen:", "Reopen Account", JOptionPane.QUESTION_MESSAGE);

            if (accountNumber != null && !accountNumber.trim().isEmpty()) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to reopen this account?",
                            "Confirm Account Reopening", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        src.services.AccountManager.getInstance()
                                .getAccountByNumber(accountNumber).closeAccount();
                        JOptionPane.showMessageDialog(this,
                                "Account reopened successfully.", "Account Reopened", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(), "Reopening Account Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handled by lambda expressions
    }
}