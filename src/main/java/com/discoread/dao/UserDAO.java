package com.discoread.dao;

import com.discoread.database.Database;
import com.discoread.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection conn = Database.getConnection();

    // ========== REGISTER USER ==========
    public boolean addUser(User user) {
        String sql = "INSERT INTO users(username, password, created_date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getCreatedDate());
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.out.println("❌ User registration failed: " + e.getMessage());
            return false;
        }
    }

    // ========== LOGIN VALIDATION ==========
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // returns true if match found
        }
        catch (SQLException e) {
            System.out.println("❌ Login query failed: " + e.getMessage());
            return false;
        }
    }

    // ========== LIST ALL USERS (OPTIONAL) ==========
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("created_date")
                );
                users.add(u);
            }
        }
        catch (SQLException e) {
            System.out.println("❌ Failed to fetch users: " + e.getMessage());
        }

        return users;
    }
}
