package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDao {

    public Account verifyAccountDetails(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

    public boolean isUser(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username = ?";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean isUser(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    
}
