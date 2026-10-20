
package dao;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {

    public boolean addTransaction(
            int accountNo,
            String type,
            double amount,
            String description) throws SQLException {

        String sql = "INSERT INTO transactions " +
                "(account_no, transaction_type, amount, description) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountNo);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setString(4, description);

            return ps.executeUpdate() > 0;
        }
    }

    public void displayTransactions(int accountNo)
            throws SQLException {

        String sql = "SELECT transaction_type, amount, " +
                "transaction_date, description FROM transactions " +
                "WHERE account_no = ? " +
                "ORDER BY transaction_id DESC LIMIT 5";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountNo);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\nLast 5 Transactions:");

                while (rs.next()) {
                    System.out.println(
                            rs.getString("transaction_type")
                            + " | Rs. " + rs.getDouble("amount")
                            + " | " + rs.getTimestamp("transaction_date")
                            + " | " + rs.getString("description"));
                }
            }
        }
    }
}
