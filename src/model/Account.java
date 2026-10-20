
package model;

import exception.InsufficientFundsException;

public abstract class Account {

    private int accountNo;
    private String holderName;
    private double balance;
    private String pin;

    private final String[] transactions = new String[5];
    private int transactionCount = 0;
    private int nextTransaction = 0;

    public Account(int accountNo, String holderName,
                   double balance, String pin) {
        this.accountNo = accountNo;
        this.holderName = holderName;
        this.balance = balance;
        this.pin = pin;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public String getPin() {
        return pin;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Deposit amount must be positive."
            );
        }

        balance += amount;
        addTransaction("Deposit: Rs. " + amount);
    }

    protected final void changeBalance(double newBalance) {
        balance = newBalance;
    }

    protected void addTransaction(String transaction) {
        transactions[nextTransaction] = transaction;
        nextTransaction = (nextTransaction + 1) % transactions.length;

        if (transactionCount < transactions.length) {
            transactionCount++;
        }
    }

    public void displayTransactions() {
        System.out.println("\nLast Transactions:");

        int start = (nextTransaction - transactionCount
                     + transactions.length) % transactions.length;

        for (int i = 0; i < transactionCount; i++) {
            int index = (start + i) % transactions.length;
            System.out.println(transactions[index]);
        }
    }

    public abstract void withdraw(double amount)
            throws InsufficientFundsException;

    public void displayDetails() {
        System.out.println("Account Number: " + accountNo);
        System.out.println("Holder Name: " + holderName);
        System.out.println("Balance: Rs. " + balance);
    }
}
