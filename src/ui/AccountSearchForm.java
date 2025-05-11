package src.ui;

import src.exceptions.InvalidAmountException;
import src.models.Account;
import src.services.AccountManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Form for searching accounts by name or number
 */
public class AccountSearchForm extends JFrame {
    private JPanel mainPanel;
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private JButton searchButton;
    private JTable resultsTable;
    private JButton backButton;
    private JLabel searchLabel;
    private JScrollPane tableScrollPane;

    private final AccountManager accountManager;
    private final DefaultTableModel tableModel;

    /**
     * Creates a new account search form
     */
    public AccountSearchForm() {
        this.accountManager = AccountManager.getInstance();
        this.tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        setupUI();
        setupActionListeners();
    }

    /**
     * Sets up the UI components
     */
    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        searchLabel = new JLabel("Search by:");
        searchTypeComboBox = new JComboBox<>(new String[]{"Account Number", "Account Holder Name"});
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        backButton = new JButton("Back");

        String[] columnNames = {"Account Number", "Account Holder", "Account Type", "Balance", "Status"};
        tableModel.setColumnIdentifiers(columnNames);
        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableScrollPane = new JScrollPane(resultsTable);

        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        buttonPanel.add(backButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setTitle("Search Accounts");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Sets up the action listeners for the buttons and table
     */
    private void setupActionListeners() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AccountInfoForm();
            }
        });

        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = resultsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
                        try {
                            Account account = accountManager.getAccountByNumber(accountNumber);
                            JOptionPane.showMessageDialog(AccountSearchForm.this,
                                    account.getAccountDetails(),
                                    "Account Details", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(AccountSearchForm.this,
                                    "Error: " + ex.getMessage(), "View Details Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
    }

    /**
     * Performs the search based on the selected criteria
     */
    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter search text", "Empty Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);

        try {
            List<Account> results;
            if (searchTypeComboBox.getSelectedIndex() == 0) {
                try {
                    Account account = accountManager.getAccountByNumber(searchText);
                    results = List.of(account);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "No accounts found with that number", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            } else {
                results = accountManager.getAccountsByName(searchText);
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No accounts found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Account account : results) {
                    tableModel.addRow(new Object[]{
                            account.getAccountNumber(),
                            account.getAccountHolderName(),
                            account.getAccountType(),
                            String.format("₱%.2f", account.getBalance()),
                            account.isActive() ? "Active" : "Closed"
                    });
                }

                JOptionPane.showMessageDialog(this,
                        results.size() + " account(s) found. Double-click a row to view details.",
                        "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(), "Search Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}