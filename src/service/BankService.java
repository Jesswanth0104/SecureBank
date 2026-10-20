
package service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import exception.InsufficientFundsException;

public class BankService {

    private final Scanner sc = new Scanner(System.in);
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    private final Map<Integer, Integer> failedAttempts = new HashMap<>();
    private final Map<Integer, Boolean> lockedAccounts = new HashMap<>();

    private Account getAuthenticatedAccount() throws SQLException {
        System.out.print("Enter account number: ");
        int accountNo = Integer.parseInt(sc.nextLine());

        if (lockedAccounts.getOrDefault(accountNo, false)) {
            System.out.println("Account is locked.");
            return null;
        }

        Account account = accountDAO.findAccount(accountNo);

        if (account == null) {
            System.out.println("Account not found.");
            return null;
        }

        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        if (!account.getPin().equals(pin)) {
            int attempts = failedAttempts.getOrDefault(accountNo, 0) + 1;
            failedAttempts.put(accountNo, attempts);

            System.out.println("Incorrect PIN.");

            if (attempts >= 3) {
                lockedAccounts.put(accountNo, true);
                System.out.println("Account locked after 3 incorrect attempts.");
            }

            return null;
        }

        failedAttempts.put(accountNo, 0);
        return account;
    }

    public void createAccount() {
        try {
            System.out.print("Enter account number: ");
            int accountNo = Integer.parseInt(sc.nextLine());

            System.out.print("Enter holder name: ");
            String name = sc.nextLine();

            System.out.print("Enter initial balance: ");
            double balance = Double.parseDouble(sc.nextLine());

            System.out.print("Enter PIN: ");
            String pin = sc.nextLine();

            System.out.println("1. Savings Account");
            System.out.println("2. Current Account");
            System.out.print("Choose account type: ");
            int type = Integer.parseInt(sc.nextLine());

            Account account;

            if (type == 1) {
                if (balance < SavingsAccount.MIN_BALANCE) {
                    System.out.println("Savings account requires minimum balance Rs. 1000.");
                    return;
                }

                account = new SavingsAccount(accountNo, name, balance, pin);

            } else if (type == 2) {
                if (balance < 0) {
                    System.out.println("Initial balance cannot be negative.");
                    return;
                }

                account = new CurrentAccount(accountNo, name, balance, pin);

            } else {
                System.out.println("Invalid account type.");
                return;
            }

            if (accountDAO.addAccount(account)) {
                System.out.println("Account created successfully!");
            }

        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    public void deposit() {
        try {
            Account account = getAuthenticatedAccount();
            if (account == null) return;

            System.out.print("Enter deposit amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            account.deposit(amount);

            accountDAO.updateBalance(account);
            transactionDAO.addTransaction(
                    account.getAccountNo(), "Deposit", amount,
                    "Money deposited");

            System.out.println("Deposit successful.");
            System.out.println("Balance: Rs. " + account.getBalance());

        } catch (Exception e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    public void withdraw() {
        try {
            Account account = getAuthenticatedAccount();
            if (account == null) return;

            System.out.print("Enter withdrawal amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            account.withdraw(amount);

            accountDAO.updateBalance(account);
            transactionDAO.addTransaction(
                    account.getAccountNo(), "Withdrawal", amount,
                    "Money withdrawn");

            System.out.println("Withdrawal successful.");
            System.out.println("Balance: Rs. " + account.getBalance());

        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal denied: " + e.getMessage());
            System.out.println("Shortfall: Rs. " + e.getShortfall());
        } catch (Exception e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    public void transfer() {
        try {
            Account sender = getAuthenticatedAccount();
            if (sender == null) return;

            System.out.print("Enter receiver account number: ");
            int receiverNo = Integer.parseInt(sc.nextLine());

            if (sender.getAccountNo() == receiverNo) {
                System.out.println("Cannot transfer to the same account.");
                return;
            }

            Account receiver = accountDAO.findAccount(receiverNo);

            if (receiver == null) {
                System.out.println("Receiver account not found.");
                return;
            }

            System.out.print("Enter transfer amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            double senderBalance = sender.getBalance();
            double receiverBalance = receiver.getBalance();

            try {
                sender.withdraw(amount);
                receiver.deposit(amount);

                accountDAO.updateBalance(sender);
                accountDAO.updateBalance(receiver);

                transactionDAO.addTransaction(
                        sender.getAccountNo(), "Transfer Out",
                        amount, "Transferred to account " + receiverNo);

                transactionDAO.addTransaction(
                        receiver.getAccountNo(), "Transfer In",
                        amount, "Received from account " + sender.getAccountNo());

                System.out.println("Transfer successful.");

            } catch (Exception e) {
                System.out.println("Transfer failed: " + e.getMessage());
                System.out.println(
                        "Important: database rollback is not implemented yet.");
            }

        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    public void viewAccount() {
        try {
            Account account = getAuthenticatedAccount();

            if (account != null) {
                account.displayDetails();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewTransactions() {
        try {
            Account account = getAuthenticatedAccount();

            if (account != null) {
                transactionDAO.displayTransactions(account.getAccountNo());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void calculateInterest() {
        try {
            Account account = getAuthenticatedAccount();

            if (account == null) return;

            if (account instanceof SavingsAccount) {
                SavingsAccount savings = (SavingsAccount) account;

                System.out.println("Yearly interest: Rs. "
                        + savings.calculateYearlyInterest());
            } else {
                System.out.println(
                        "Interest is available only for Savings Accounts.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
