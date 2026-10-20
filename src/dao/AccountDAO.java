
package dao;

import database.DBConnection;
import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public boolean addAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts " +
                "(account_no, holder_name, account_type, balance, pin) " +
                "VALUES (?, ?, ?, ?, ?)";

        String type = account instanceof SavingsAccount
                ? "Savings" : "Current";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, account.getAccountNo());
            ps.setString(2, account.getHolderName());
            ps.setString(3, type);
            ps.setDouble(4, account.getBalance());
            ps.setString(5, account.getPin());

            return ps.executeUpdate() > 0;
        }
    }

    public Account findAccount(int accountNo) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("account_type");
                    String name = rs.getString("holder_name");
                    double balance = rs.getDouble("balance");
                    String pin = rs.getString("pin");

                    if ("Savings".equalsIgnoreCase(type)) {
                        return new SavingsAccount(
                                accountNo, name, balance, pin);
                    } else if ("Current".equalsIgnoreCase(type)) {
                        return new CurrentAccount(
                                accountNo, name, balance, pin);
                    }
                }
            }
        }

        return null;
    }

    public boolean updateBalance(Account account) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? " +
                "WHERE account_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, account.getBalance());
            ps.setInt(2, account.getAccountNo());

            return ps.executeUpdate() > 0;
        }
    }
}
