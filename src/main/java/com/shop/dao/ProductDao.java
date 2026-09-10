package com.shop.dao;

import com.shop.model.Product;
import com.shop.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public List<Product> findAll() throws SQLException {
        List<Product> result = new ArrayList<>();
        String sql = "SELECT id, name, description, price, quantity FROM products ORDER BY id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet r = s.executeQuery()) {
            while (r.next()) {
                result.add(map(r));
            }
        }
        return result;
    }

    public Product findById(long id) throws SQLException {
        String sql = "SELECT id, name, description, price, quantity FROM products WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setLong(1, id);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    return null;
                }
                return map(r);
            }
        }
    }

    public void create(String name, String description, BigDecimal price, int quantity) throws SQLException {
        String sql = "INSERT INTO products(name, description, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, name);
            s.setString(2, description);
            s.setBigDecimal(3, price);
            s.setInt(4, quantity);
            s.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setLong(1, id);
            s.executeUpdate();
        }
    }

    private Product map(ResultSet r) throws SQLException {
        Product p = new Product();
        p.setId(r.getLong(1));
        p.setName(r.getString(2));
        p.setDescription(r.getString(3));
        p.setPrice(r.getBigDecimal(4));
        p.setQuantity(r.getInt(5));
        return p;
    }
}
