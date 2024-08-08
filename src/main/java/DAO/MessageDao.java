package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

/**
 * Data Access Object (DAO) for managing messages in the database.
 */
public class MessageDao {

    /**
     * Adds a new message to the database.
     * @param message The Message object containing the message details to be added.
     * @return The newly created Message object with the generated ID, or null if the insertion failed.
     */
    public Message addMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int msgId = (int) rs.getLong(1);
                return new Message(msgId, message.posted_by, message.message_text, message.time_posted_epoch);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a message from the database by its ID.
     * @param id The ID of the message to retrieve.
     * @return The Message object with the specified ID, or null if not found.
     */
    public Message getMessage(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from message where message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Message(rs.getInt("message_id"), 
                                   rs.getInt("posted_by"),
                                   rs.getString("message_text"),
                                   rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all messages from the database.
     * @return A list of all Message objects in the database.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                messages.add(new Message(rs.getInt("message_id"), 
                                         rs.getInt("posted_by"), 
                                         rs.getString("message_text"),
                                         rs.getLong("time_posted_epoch")));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Retrieves all messages posted by a specific account from the database.
     * @param account_id The ID of the account whose messages to retrieve.
     * @return A list of Message objects for the specified account.
     */
    public List<Message> getAllMessages(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message where posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                messages.add(new Message(rs.getInt("message_id"), 
                                         rs.getInt("posted_by"), 
                                         rs.getString("message_text"),
                                         rs.getLong("time_posted_epoch")));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Removes a message from the database by its ID.
     * @param message_id The ID of the message to delete.
     */
    public void removeMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "delete from message where message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the text of an existing message by its ID.
     * @param message_id The ID of the message to update.
     * @param message The Message object containing the new message text.
     */
    public void updateMessage(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "update message set message_text = ? where message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.message_text);
            ps.setInt(2, message_id);
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}