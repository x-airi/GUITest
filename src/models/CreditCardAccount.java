package src.models;

import src.exceptions.InvalidAmountException;
import src.exceptions.InsufficientFundsException;
import src.exceptions.TransactionLimitException;

/**
 * Represents a credit card account with a credit limit
 */
public class CreditCardAccount extends Account {
    private final double creditLimit;
    private double interestRate;
    private static final double DEFAULT_INTEREST_RATE = 18.99;
    private static final double MIN_PAYMENT_PERCENTAGE = 0.02; // 2% of balance
    
    /**
     * Creates a new credit card account with the specified details
     *
     * @param accountHolderName the name of the account holder
     * @param creditLimit the credit limit
     * @throws InvalidAmountException if the credit limit is invalid
     */
    public CreditCardAccount(String accountHolderName, double creditLimit) throws InvalidAmountException {
        super(accountHolderName, 0); // Credit cards start with 0 balance
        if (creditLimit <= 0) {
            throw new InvalidAmountException("Credit limit must be positive");
        }
        
        this.creditLimit = creditLimit;
        this.interestRate = DEFAULT_INTEREST_RATE;
    }
    
    /**
     * Gets the credit limit
     *
     * @return the credit limit
     */
    public double getCreditLimit() {
        return creditLimit;
    }
    
    /**
     * Gets the interest rate
     *
     * @return the interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }
    
    /**
     * Sets the interest rate
     *
     * @param interestRate the new interest rate
     */
    public void setInterestRate(double interestRate) {
        if (interestRate >= 0) {
            this.interestRate = interestRate;
            logTransaction("Interest Rate Change", 0, 
                    String.format("Interest rate changed to %.2f%%", interestRate), balance);
        }
    }
    
    /**
     * Gets the available credit
     *
     * @return the available credit
     */
    public double getAvailableCredit() {
        return creditLimit + balance; // balance is negative for credit card debt
    }
    
    /**
     * Gets the current debt (negative of balance)
     *
     * @return the current debt amount
     */
    public double getCurrentDebt() {
        return -balance; // Convert to positive for clearer understanding
    }
    
    /**
     * Gets the minimum payment due
     *
     * @return the minimum payment amount
     */
    public double getMinimumPaymentDue() {
        return Math.max(getCurrentDebt() * MIN_PAYMENT_PERCENTAGE, 20.0); // At least ₱20 or 2% of balance
    }
    
    @Override
    public void deposit(double amount) throws Exception {
        // For credit cards, deposit means making a payment
        super.deposit(amount);
        logTransaction("Payment", amount, "Credit card payment received", balance);
    }
    
    @Override
    public void withdraw(double amount) throws Exception {
        // For credit cards, withdraw means making a purchase (charging the card)
        if (amount <= 0) {
            throw new InvalidAmountException("Purchase amount must be positive");
        }
        
        if (amount > getAvailableCredit()) {
            throw new TransactionLimitException(amount, getAvailableCredit());
        }
        
        balance -= amount; // Decrease balance (increase debt)
        logTransaction("Purchase", amount, "Credit card purchase", balance);
    }
    
    /**
     * Makes a purchase with the credit card
     *
     * @param amount the purchase amount
     * @param description the purchase description
     * @throws Exception if the purchase cannot be processed
     */
    public void makePurchase(double amount, String description) throws Exception {
        if (amount <= 0) {
            throw new InvalidAmountException("Purchase amount must be positive");
        }
        
        if (amount > getAvailableCredit()) {
            throw new TransactionLimitException(amount, getAvailableCredit());
        }
        
        balance -= amount; // Decrease balance (increase debt)
        logTransaction("Purchase", amount, description, balance);
    }
    
    /**
     * Makes a payment toward the credit card balance
     *
     * @param amount the payment amount
     * @throws Exception if the payment cannot be processed
     */
    public void makePayment(double amount) throws Exception {
        if (amount <= 0) {
            throw new InvalidAmountException("Payment amount must be positive");
        }
        
        if (amount > getCurrentDebt()) {
            throw new InvalidAmountException("Payment amount exceeds current debt");
        }
        
        balance += amount; // Increase balance (decrease debt)
        logTransaction("Payment", amount, "Credit card payment", balance);
    }
    
    /**
     * Applies monthly interest to the account
     */
    public void applyMonthlyInterest() {
        if (balance < 0) { // Only apply interest if there's a debt
            double interestAmount = -(balance) * (interestRate / 100 / 12); // Monthly interest
            balance -= interestAmount; // Decrease balance (increase debt)
            logTransaction("Interest Charge", interestAmount, 
                    String.format("Monthly interest at %.2f%%", interestRate), balance);
        }
    }

    @Override
    public void logTransaction(String transactionType, double amount, String description) {
        super.logTransaction(transactionType, amount, description, balance);
    }

    @Override
    public String getAccountType() {
        return "Credit Card Account";
    }
    
    @Override
    public boolean verifyAccountDetails() {
        return super.verifyAccountDetails() && creditLimit > 0;
    }
}
