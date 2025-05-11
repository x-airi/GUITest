package src.interfaces;

/**
 * Interface for accounts that can earn interest.
 * This interface provides methods to manage interest rates and apply interest to the account.
 */
public interface InterestBearing {

    /**
     * Gets the current interest rate for the account.
     *
     * @return the interest rate as a percentage (e.g., 2.5 for 2.5%).
     */
    double getInterestRate();

    /**
     * Sets the interest rate for the account.
     *
     * @param rate the interest rate as a percentage.
     */
    void setInterestRate(double rate);

    /**
     * Calculates and applies interest to the account.
     *
     * @return the amount of interest added to the account.
     */
    double applyInterest();
}