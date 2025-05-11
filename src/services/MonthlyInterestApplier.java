package src.services;

import src.exceptions.InvalidAmountException;
import src.models.InvestmentAccount;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Service for applying monthly interest to investment accounts.
 */
public class MonthlyInterestApplier {
    private final AccountManager accountManager;
    private Timer timer;

    /**
     * Creates a new monthly interest applier.
     */
    public MonthlyInterestApplier() {
        this.accountManager = AccountManager.getInstance();
    }

    /**
     * Applies interest to all eligible investment accounts.
     */
    public void applyMonthlyInterest() {
        accountManager.applyMonthlyInterestToAllAccounts();
        accountManager.applyMonthlyInterestToCreditCards();
        System.out.println("Applied monthly interest on " + LocalDate.now());
    }

    /**
     * Starts the monthly interest application scheduler.
     */
    public void startMonthlyInterestApplication() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                applyMonthlyInterest();
            }
        }, getTimeUntilNextMonthFirstDay(), 30L * 24 * 60 * 60 * 1000); // Run approximately every 30 days
    }

    /**
     * Stops the monthly interest application scheduler.
     */
    public void stopMonthlyInterestApplication() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Calculates the delay until the first day of the next month.
     *
     * @return the delay in milliseconds
     */
    private long getTimeUntilNextMonthFirstDay() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfNextMonth = today.plusMonths(1).withDayOfMonth(1);
        return java.time.Duration.between(
                today.atStartOfDay(), firstDayOfNextMonth.atStartOfDay()).toMillis();
    }

    /**
     * Manually applies interest to a specific investment account.
     *
     * @param account the investment account
     * @return the interest amount applied
     */
    public double applyInterestToAccount(InvestmentAccount account) {
        return account.applyInterest();
    }
}