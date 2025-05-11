package src.ui;

import src.exceptions.InvalidAmountException;
import src.models.Account;
import src.services.AccountManager;
import src.services.MonthlyInterestApplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main GUI class for the banking application
 */
public class AccountsMain {
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

    private final AccountManager accountManager;
    private final MonthlyInterestApplier interestApplier;
    private JFrame frame;

    /**
     * Creates the main GUI
     */
    public AccountsMain() {
        AccountManager tempManager = AccountManager.getInstance();
        this.accountManager = tempManager;
        this.interestApplier = new MonthlyInterestApplier();

        setupUI();
        setupActionListeners();
    }

    /**
     * Sets up the UI components
     */
    private void setupUI() {
        frame = new JFrame("Banking System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);

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

        gbc.gridy = 5;
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

        frame.setContentPane(mainPanel);
        frame.setTitle("SABOG FINANCE");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Creates a styled button with default properties
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
     * Sets up the action listeners for the buttons
     */
    private void setupActionListeners() {
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new AccountTypes();
            }
        });

        balanceInquiryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = JOptionPane.showInputDialog(frame,
                        "Enter account number:", "Balance Inquiry", JOptionPane.QUESTION_MESSAGE);

                if (accountNumber != null && !accountNumber.trim().isEmpty()) {
                    try {
                        double balance = accountManager.getAccountByNumber(accountNumber).getBalance();
                        JOptionPane.showMessageDialog(frame,
                                String.format("Current balance: ₱%.2f", balance),
                                "Balance Inquiry", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Error: " + ex.getMessage(), "Balance Inquiry Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new TransactionForm("Deposit");
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new TransactionForm("Withdraw");
            }
        });

        moneyTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new TransactionForm("Transfer");
            }
        });

        accountInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new AccountInfoForm();
            }
        });

        closeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = JOptionPane.showInputDialog(frame,
                        "Enter account number to close:", "Close Account", JOptionPane.QUESTION_MESSAGE);

                if (accountNumber != null && !accountNumber.trim().isEmpty()) {
                    try {
                        int confirm = JOptionPane.showConfirmDialog(frame,
                                "Are you sure you want to close this account?",
                                "Confirm Account Closure", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (confirm == JOptionPane.YES_OPTION) {
                            accountManager.getAccountByNumber(accountNumber).closeAccount();
                            JOptionPane.showMessageDialog(frame,
                                    "Account closed successfully.", "Account Closed", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Error: " + ex.getMessage(), "Close Account Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        reopenAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = JOptionPane.showInputDialog(frame,
                        "Enter account number to reopen:", "Reopen Account", JOptionPane.QUESTION_MESSAGE);

                if (accountNumber != null && !accountNumber.trim().isEmpty()) {
                    try {
                        int confirm = JOptionPane.showConfirmDialog(frame,
                                "Are you sure you want to reopen this account?",
                                "Confirm Account Reopening", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (confirm == JOptionPane.YES_OPTION) {
                            Account account = accountManager.getAccountByNumber(accountNumber);
                            account.reopenAccount();
                            JOptionPane.showMessageDialog(frame,
                                    "Account reopened successfully.", "Account Reopened", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Error: " + ex.getMessage(), "Reopening Account Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit?", "Confirm Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Shows the main GUI screen
     */
    public void show() {
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}