package com.inno.webproject.dao;

import com.inno.webproject.model.User;
import com.inno.webproject.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private static final String SQL_FIND_BY_USERNAME =
            "SELECT id, username, password, email, role FROM users WHERE username = ?";
    private static final String SQL_INSERT_USER =
            "INSERT INTO users(username, password, email, role) VALUES (?, ?, ?, 'USER')";
    private static final String SQL_UPDATE_EMAIL =
            "UPDATE users SET email = ? WHERE id = ?";

    public User findByUsername(String username) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_FIND_BY_USERNAME)) {
            s.setString(1, username);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    return null;
                }
                return map(r);
            }
        }
    }

    public void create(User u) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_INSERT_USER)) {
            s.setString(1, u.username());
            s.setString(2, u.password());
            s.setString(3, u.email());
            s.executeUpdate();
        }
    }

    public void updateEmail(long id, String email) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_UPDATE_EMAIL)) {
            s.setString(1, email);
            s.setLong(2, id);
            s.executeUpdate();
        }
    }

    private User map(ResultSet r) throws SQLException {
        return new User(
                r.getLong("id"),
                r.getString("username"),
                r.getString("password"),
                r.getString("email"),
                r.getString("role")
        );
    }
}
