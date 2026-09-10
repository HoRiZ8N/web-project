package com.inno.webproject.dao;

import com.inno.webproject.model.Product;
import com.inno.webproject.util.DatabaseConnection;
import com.inno.webproject.util.DatabasePoolException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private static final String SQL_FIND_ALL =
            "SELECT id, name, description, price, quantity FROM products ORDER BY id";
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, description, price, quantity FROM products WHERE id = ?";
    private static final String SQL_INSERT_PRODUCT =
            "INSERT INTO products(name, description, price, quantity) VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE_PRODUCT =
            "DELETE FROM products WHERE id = ?";

    public List<Product> findAll() throws SQLException, DatabasePoolException {
        List<Product> result = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_FIND_ALL);
             ResultSet r = s.executeQuery()) {
            while (r.next()) {
                result.add(map(r));
            }
        }
        return result;
    }

    public Product findById(long id) throws SQLException, DatabasePoolException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_FIND_BY_ID)) {
            s.setLong(1, id);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    return null;
                }
                return map(r);
            }
        }
    }

    public void create(String name, String description, BigDecimal price, int quantity) throws SQLException, DatabasePoolException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_INSERT_PRODUCT)) {
            s.setString(1, name);
            s.setString(2, description);
            s.setBigDecimal(3, price);
            s.setInt(4, quantity);
            s.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException, DatabasePoolException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_DELETE_PRODUCT)) {
            s.setLong(1, id);
            s.executeUpdate();
        }
    }

    private Product map(ResultSet r) throws SQLException {
        return new Product(
                r.getLong(1),
                r.getString(2),
                r.getString(3),
                r.getBigDecimal(4),
                r.getInt(5)
        );
    }
}
