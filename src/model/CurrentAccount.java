
package model;

import exception.InsufficientFundsException;

public class CurrentAccount extends Account {

    public static final double OVERDRAFT_LIMIT = 5000.0;

    public CurrentAccount(int accountNo, String holderName,
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

        if (remainingBalance < -OVERDRAFT_LIMIT) {
            double shortfall = remainingBalance
                               + OVERDRAFT_LIMIT;

            throw new InsufficientFundsException(
                "Current account overdraft limit is Rs. 5000.",
                shortfall
            );
        }

        changeBalance(remainingBalance);
        addTransaction("Withdrawal: Rs. " + amount);
    }

    @Override
    public void displayDetails() {
        System.out.println("\nCurrent Account Details");
        super.displayDetails();
        System.out.println(
            "Overdraft Limit: Rs. " + OVERDRAFT_LIMIT
        );
    }
}
