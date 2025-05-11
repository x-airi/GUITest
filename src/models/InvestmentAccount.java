package src.models;

import src.exceptions.InvalidAmountException;
import src.interfaces.InterestBearing;

/**
 * Represents an investment account with interest
 */
public class InvestmentAccount extends Account implements InterestBearing {
    private double interestRate;
    private static final double DEFAULT_INTEREST_RATE = 2.5; // 2.5%
    private static final double MIN_BALANCE_FOR_INTEREST = 1000.0;
    
    /**
     * Creates a new investment account with the specified details
     *
     * @param accountHolderName the name of the account holder
     * @param initialDeposit the initial deposit amount
     * @throws InvalidAmountException if the initial deposit is invalid
     */
    public InvestmentAccount(String accountHolderName, double initialDeposit) throws InvalidAmountException {
        super(accountHolderName, initialDeposit);
        this.interestRate = DEFAULT_INTEREST_RATE;
    }
    
    @Override
    public double getInterestRate() {
        return interestRate;
    }
    
    @Override
    public void setInterestRate(double rate) {
        if (rate >= 0) {
            this.interestRate = rate;
            logTransaction("Interest Rate Change", 0, 
                    String.format("Interest rate changed to %.2f%%", rate), balance);
        }
    }
    
    @Override
    public double applyInterest() {
        if (balance >= MIN_BALANCE_FOR_INTEREST) {
            double interestAmount = balance * (interestRate / 100 / 12); // Monthly interest
            balance += interestAmount;
            logTransaction("Interest", interestAmount, 
                    String.format("Monthly interest at %.2f%%", interestRate), balance);
            return interestAmount;
        }
        return 0;
    }
    
    /**
     * Gets the minimum balance required to earn interest
     *
     * @return the minimum balance
     */
    public double getMinBalanceForInterest() {
        return MIN_BALANCE_FOR_INTEREST;
    }

    @Override
    public void logTransaction(String transactionType, double amount, String description) {
        super.logTransaction(transactionType, amount, description, balance);
    }

    @Override
    public String getAccountType() {
        return "Investment Account";
    }
    
    @Override
    public boolean verifyAccountDetails() {
        return super.verifyAccountDetails() && interestRate >= 0;
    }
}
