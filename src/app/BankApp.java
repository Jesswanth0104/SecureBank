package app;

import java.util.Scanner;
import service.BankService;

public class BankApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankService();

        int choice = 0;

        do {
            System.out.println("\n===== SECUREBANK =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Account Details");
            System.out.println("6. View Transaction History");
            System.out.println("7. Calculate Savings Interest");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        bankService.createAccount();
                        break;

                    case 2:
                        bankService.deposit();
                        break;

                    case 3:
                        bankService.withdraw();
                        break;

                    case 4:
                        bankService.transfer();
                        break;

                    case 5:
                        bankService.viewAccount();
                        break;

                    case 6:
                        bankService.viewTransactions();
                        break;

                    case 7:
                        bankService.calculateInterest();
                        break;

                    case 8:
                        System.out.println(
                            "Thank you for using SecureBank!"
                        );
                        break;

                    default:
                        System.out.println(
                            "Invalid choice! Enter a number from 1 to 8."
                        );
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                choice = 0;
            }

        } while (choice != 8);

        sc.close();
    }
}