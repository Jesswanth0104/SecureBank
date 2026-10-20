
package model;

import exception.InsufficientFundsException;

public class SavingsAccount extends Account {

    public static final double MIN_BALANCE = 1000.0;
    private static final double INTEREST_RATE = 0.04;

    public SavingsAccount(int accountNo, String holderName,
                          double balance, String pin) {
        super(accountNo, holderName, balance, pin);
    }

    @Override
    public void withdraw(double amount)
            throws InsufficientFundsException {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Withdrawal amount must be positive."
            );
        }

        double remainingBalance = getBalance() - amount;

        if (remainingBalance < MIN_BALANCE) {
            double shortfall = MIN_BALANCE - remainingBalance;

            throw new InsufficientFundsException(
                "Savings account must keep Rs. 1000 minimum.",
                shortfall
            );
        }

        changeBalance(remainingBalance);
        addTransaction("Withdrawal: Rs. " + amount);
    }

    public double calculateYearlyInterest() {
        return getBalance() * INTEREST_RATE;
    }

    @Override
    public void displayDetails() {
        System.out.println("\nSavings Account Details");
        super.displayDetails();
        System.out.println("Minimum Balance: Rs. " + MIN_BALANCE);
    }
}
