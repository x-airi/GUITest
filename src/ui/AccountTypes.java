package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI for selecting account types.
 * This class provides a user interface for the user to select the type of account they wish to create.
 */
public class AccountTypes implements ActionListener {
    private JPanel typesPanel;
    private JButton bankAccountsButton;
    private JButton investmentAccountsButton;
    private JButton checkingAccountsButton;
    private JButton creditCardAccountsButton;
    private JButton backButton;
    JFrame typeFrame = new JFrame();

    /**
     * Creates a new account types selection screen.
     * Initializes the GUI components and sets up action listeners for the buttons.
     */
    public AccountTypes() {
        typeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        typeFrame.setUndecorated(false);

        typesPanel = new JPanel(new GridBagLayout());

        Font buttonFont = new Font("Arial", Font.BOLD, 30);

        bankAccountsButton = createStyledButton("Bank Accounts", buttonFont);
        checkingAccountsButton = createStyledButton("Checking Accounts", buttonFont);
        investmentAccountsButton = createStyledButton("Investment Accounts", buttonFont);
        creditCardAccountsButton = createStyledButton("Credit Card Accounts", buttonFont);
        backButton = createStyledButton("Back", buttonFont);

        JLabel titleLabel = new JLabel("Account Types", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        typesPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        typesPanel.add(bankAccountsButton, gbc);

        gbc.gridy = 2;
        typesPanel.add(investmentAccountsButton, gbc);

        gbc.gridy = 3;
        typesPanel.add(checkingAccountsButton, gbc);

        gbc.gridy = 4;
        typesPanel.add(creditCardAccountsButton, gbc);

        gbc.gridy = 5;
        typesPanel.add(backButton, gbc);

        typeFrame.setContentPane(typesPanel);
        typeFrame.setTitle("SABOG FINANCE");
        typeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        typeFrame.setLocationRelativeTo(null);
        typeFrame.setVisible(true);

        setupActionListeners();
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
        backButton.addActionListener(e -> {
            typeFrame.dispose();
            new AccountsMain(); // Go back to AccountsMain
        });

        bankAccountsButton.addActionListener(e -> {
            typeFrame.dispose();
            new AccountCreationForm("Bank Account");
        });

        checkingAccountsButton.addActionListener(e -> {
            typeFrame.dispose();
            new AccountCreationForm("Checking Account");
        });

        investmentAccountsButton.addActionListener(e -> {
            typeFrame.dispose();
            new AccountCreationForm("Investment Account");
        });

        creditCardAccountsButton.addActionListener(e -> {
            typeFrame.dispose();
            new AccountCreationForm("Credit Card Account");
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handled by anonymous inner classes
    }
}