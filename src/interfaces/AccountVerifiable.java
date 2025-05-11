package src.interfaces;

/**
 * Interface for account verification operations.
 * This interface defines the method required to verify the validity of account details.
 */
public interface AccountVerifiable {

    /**
     * Verifies if account details are valid.
     * @return true if account details are valid, false otherwise.
     */
    boolean verifyAccountDetails();
}