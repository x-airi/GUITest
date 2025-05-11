package src;
/**
 * 9402 IT122
 * Final Project
 * Leader: Bimmuyag, Ashel
 * Members:
 * Balunatse, Denver
 * Lavarias, Gino
 * Manalili, Meyielle
 * Rivera, Sherlie
 * Ugay, Anton
 * Valdriz, Jake
 */

import src.exceptions.InvalidAmountException;
import src.services.AccountManager;
import src.services.MonthlyInterestApplier;
import src.ui.AccountsMain;
import src.ui.LoginForm;
import src.ui.LoginListener;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The MainApplication class serves as the entry point for the application.
 * It initializes the AccountManager, starts the monthly interest application,
 * and launches the login form for user authentication.
 */
public abstract class MainApplication {
    private static LoginListener loginListener;

    /**
     * The main method that runs the application.
     * It creates an instance of AccountManager, loads accounts,
     * starts the monthly interest application, and displays the login form.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Create the AccountManager instance and load accounts
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AccountManager accountManager = AccountManager.getInstance();
                // Load accounts on startup
                accountManager.loadAccounts();

                // Start monthly interest applier
                MonthlyInterestApplier interestApplier = new MonthlyInterestApplier();
                interestApplier.startMonthlyInterestApplication();

                // Launch login form
                new LoginForm(new LoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        // Show the Accounts Main GUI after successful login
                        AccountsMain accountsMain = new AccountsMain();
                        // Add window listener to save accounts on exit
                        JFrame frame = accountsMain.getFrame();
                        frame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                accountManager.saveAccounts(); // Save accounts on exit
                                System.exit(0);
                            }
                        });
                    }
                });
            }
        });
    }
}