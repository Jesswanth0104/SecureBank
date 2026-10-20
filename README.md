# SecureBank – Banking Management System

## About the Project

SecureBank is a console-based banking application developed using Java and MySQL. It allows users to create bank accounts, deposit and withdraw money, transfer funds, view account details, and check transaction history.

The project demonstrates Java Object-Oriented Programming (OOP) concepts and database connectivity using JDBC.

## Features

- Create Savings and Current accounts
- Deposit money into an account
- Withdraw money based on account rules
- Transfer money between accounts
- View account details
- View the last 5 transactions
- Calculate yearly interest for Savings Accounts
- PIN verification before banking operations
- Lock an account after 3 incorrect PIN attempts during the current application session
- Store account and transaction details in MySQL

## Technologies Used

- **Java** – Application development
- **MySQL** – Database management
- **JDBC** – Java database connectivity
- **MySQL Connector/J** – JDBC driver
- **VS Code** – Development environment
- **Git and GitHub** – Version control

## Account Rules

| Feature | Savings Account | Current Account |
|---|---|---|
| Minimum balance | ₹1,000 | No minimum balance specified |
| Overdraft facility | Not allowed | Up to ₹5,000 |
| Yearly interest | 4% | Not applicable |

## Project Structure

```text
SecureBank/
├── src/
│   ├── app/
│   │   └── BankApp.java
│   ├── database/
│   │   └── DBConnection.java
│   ├── dao/
│   │   ├── AccountDAO.java
│   │   └── TransactionDAO.java
│   ├── exception/
│   │   └── InsufficientFundsException.java
│   ├── model/
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   └── CurrentAccount.java
│   └── service/
│       └── BankService.java
├── sql/
│   └── securebank.sql
├── lib/
│   └── mysql-connector-j-26.7.0/
│       └── mysql-connector-j-26.7.0.jar
├── out/
└── README.md
```

## Database Setup

### 1. Create the database

Open MySQL Workbench and execute the SQL script located at `sql/securebank.sql`.

The database is named `securebank` and contains two tables:

- `accounts` – Stores account number, holder name, account type, balance, PIN, and account lock information.
- `transactions` – Stores transaction type, amount, date, description, and account number.

### 2. Configure the database connection

Open `src/database/DBConnection.java` and configure your own MySQL username and password.

Make sure MySQL Server is running before starting the application.

## How to Run

### Prerequisites

- Java JDK installed
- MySQL Server and MySQL Workbench installed
- MySQL Connector/J JAR available in the `lib` directory

### Step 1: Open the project

Open the SecureBank project folder in VS Code.

### Step 2: Compile the Java files

Run this command in the terminal from the project root directory:

```powershell
javac -cp ".\lib\mysql-connector-j-26.7.0\mysql-connector-j-26.7.0.jar" -d out src\database\DBConnection.java src\exception\InsufficientFundsException.java src\model\Account.java src\model\SavingsAccount.java src\model\CurrentAccount.java src\dao\AccountDAO.java src\dao\TransactionDAO.java src\service\BankService.java src\app\BankApp.java
```

### Step 3: Run the application

```powershell
java -cp "out;.\lib\mysql-connector-j-26.7.0\mysql-connector-j-26.7.0.jar" app.BankApp
```

## Application Menu

When the application starts, the following menu is displayed:

```text
===== SECUREBANK =====
1. Create Account
2. Deposit Money
3. Withdraw Money
4. Transfer Money
5. View Account Details
6. View Transaction History
7. Calculate Savings Interest
8. Exit
```

Enter a menu number to perform the required operation.

## Java Concepts Used

- **Classes and Objects:** Represent bank accounts and banking operations.
- **Inheritance:** `SavingsAccount` and `CurrentAccount` extend `Account`.
- **Method Overriding:** Account types implement their own withdrawal rules.
- **Polymorphism:** Different account types are handled through the `Account` reference.
- **Encapsulation:** Account data is managed using private fields and methods.
- **Constructors and `super`:** Initialize account objects through constructor chaining.
- **Exception Handling:** Handle insufficient funds, invalid inputs, and database errors.
- **Collections:** Track failed PIN attempts during the current session.
- **Arrays:** Store the last five transactions in the account model.
- **JDBC:** Connect Java to MySQL and execute database queries.

## Future Improvements

- Store failed PIN attempts and account lock status persistently in MySQL.
- Use secure PIN hashing instead of storing PINs as plain text.
- Use database transactions to make fund transfers atomic.
- Improve input validation and error handling.

## Author

Developed as a college project to practice Java, Object-Oriented Programming, JDBC, and MySQL.
