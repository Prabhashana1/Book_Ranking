/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.megasoftware.bookranking;

import java.sql.*;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author prabhashana
 */
public class UserLogin {
    
    private static final String URL = "jdbc:mysql://localhost:3306/book_ranking";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("password");

                if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                    return true; // Passwords match
                }
            }
        } catch (SQLException e) {
            LoginFrame loginFrame = new LoginFrame();
            JOptionPane.showMessageDialog(loginFrame, e);
        }
        return false; // Invalid username or password
    }
    
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    public void createUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, hashPassword(password));

            // Execute query
            statement.executeUpdate();
            AddUserFrame addUserFrame = new AddUserFrame();
            JOptionPane.showMessageDialog(addUserFrame, "User add successfully");
        } catch (SQLException e) {
            AddUserFrame addUserFrame = new AddUserFrame();
            JOptionPane.showMessageDialog(addUserFrame, e);
        }
    }


}
