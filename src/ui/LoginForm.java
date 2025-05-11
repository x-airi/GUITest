package src.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Login form for bank clerks.
 * This class provides a user interface for bank clerks to log in to the system.
 */
public class LoginForm extends JFrame {
    private JPanel mainPanel;
    private JTextField accountField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel forgotPasswordLabel;
    private LoginListener loginListener; // Listener for login success

    /**
     * Creates a new login form.
     *
     * @param listener the listener to notify on successful login
     */
    public LoginForm(LoginListener listener) {
        this.loginListener = listener; // Set the listener
        setupUI();
        setupActionListeners();
    }

    /**
     * Sets up the UI components.
     * This method initializes the components of the login form.
     */
    private void setupUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the JFrame
        setUndecorated(false); // Ensure the frame has decorations (title bar, borders)

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 250, 255)); // Set background color

        JLabel titleLabel = new JLabel("Bank Clerk Log In", JLabel.CENTER);
        JLabel accountLabel = new JLabel("Account:");
        JLabel passwordLabel = new JLabel("Password:");
        accountField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = createStyledButton("Log In");
        forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>", JLabel.CENTER);
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        titleLabel.setFont(new Font("Arial", Font.BOLD, 65)); // Increased font size
        accountLabel.setFont(new Font("Arial", Font.BOLD, 50)); // Increased font size
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 50)); // Increased font size
        loginButton.setFont(new Font("Arial", Font.BOLD, 30)); // Increased font size
        forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size

        accountField.setFont(new Font("Arial", Font.PLAIN, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased padding around components
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; // Allow buttons to expand horizontally
        gbc.weighty = 1.0; // Allow buttons to expand vertically

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(accountLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(accountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        mainPanel.add(forgotPasswordLabel, gbc);

        setContentPane(mainPanel);
        setTitle("Bank Clerk Log In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        button.setFont(new Font("Arial", Font.BOLD, 30)); // Increased font size
        button.setBackground(new Color(163, 41, 41));
        button.setForeground(Color.BLACK); // Change text color to black
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(200, 60)); // Set preferred size for the button
        return button;
    }

    /**
     * Sets up the action listeners for the buttons.
     * This method defines the actions that occur when the login button is clicked.
     */
    private void setupActionListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = accountField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                if (validateLogin(account, password)) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loginListener.onLoginSuccess(); // Notify listener on successful login
                    dispose(); // Close the login form
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid account or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Please seek your supervisor for password recovery.",
                        "Password Recovery",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Validates the login credentials against stored accounts.
     *
     * @param account  the account name
     * @param password the password
     * @return true if the credentials are valid, false otherwise
     */
    private boolean validateLogin(String account, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/bank_clerks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(account) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}