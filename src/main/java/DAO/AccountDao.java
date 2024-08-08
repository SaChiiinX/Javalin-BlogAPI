package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

/**
 * Data Access Object (DAO) for managing user accounts in the database.
 */
public class AccountDao {

    /**
     * Verifies the account details by checking the provided username and password.
     * @param account The Account object containing the username and password to verify.
     * @return The Account object if the credentials are valid, or null if they are invalid.
     */
    public Account verifyAccountDetails(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.username);
            ps.setString(2, account.password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt("account_id"),
                                   rs.getString("username"), 
                                   rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Registers a new user account in the database.
     * @param account The Account object containing the username and password to register.
     * @return The newly created Account object with the generated ID, or null if the registration failed.
     */
    public Account registerUser(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account (username, password) values (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.username);
            ps.setString(2, account.password);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int accId = (int) rs.getLong(1);
                return new Account(accId, account.username, account.password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Checks if a user with the given username exists in the database.
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean isUser(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Checks if a user with the given account ID exists in the database.
     * @param posted_by The account ID to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean isUser(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}