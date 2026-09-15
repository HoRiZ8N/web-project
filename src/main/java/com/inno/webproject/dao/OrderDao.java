package com.inno.webproject.dao;

import com.inno.webproject.model.Order;
import com.inno.webproject.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private static final String SQL_LOCK_PRODUCT_QUANTITY =
            "SELECT quantity FROM products WHERE id = ? FOR UPDATE";
    private static final String SQL_INSERT_ORDER =
            "INSERT INTO orders(user_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";
    private static final String SQL_DECREASE_PRODUCT_QUANTITY =
            "UPDATE products SET quantity = quantity - ? WHERE id = ?";
    private static final String SQL_FIND_ORDERS_BY_USER =
            "SELECT o.id, o.user_id, o.product_id, p.name, o.quantity, o.total_price, o.status, o.created_at " +
                    "FROM orders o JOIN products p ON p.id = o.product_id " +
                    "WHERE o.user_id = ? ORDER BY o.created_at DESC";
    private static final String SQL_LOCK_ORDER_FOR_CANCEL =
            "SELECT product_id, quantity FROM orders WHERE id = ? AND user_id = ? AND status = 'CREATED' FOR UPDATE";
    private static final String SQL_CANCEL_ORDER =
            "UPDATE orders SET status = 'CANCELLED' WHERE id = ? AND user_id = ?";
    private static final String SQL_INCREASE_PRODUCT_QUANTITY =
            "UPDATE products SET quantity = quantity + ? WHERE id = ?";

    public void create(long user, long product, int qty, BigDecimal total) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement s = c.prepareStatement(SQL_LOCK_PRODUCT_QUANTITY)) {
                s.setLong(1, product);
                try (ResultSet r = s.executeQuery()) {
                    if (!r.next() || r.getInt(1) < qty) {
                        throw new IllegalArgumentException("Not enough product quantity available");
                    }
                }
            }

            try (PreparedStatement s = c.prepareStatement(SQL_INSERT_ORDER)) {
                s.setLong(1, user);
                s.setLong(2, product);
                s.setInt(3, qty);
                s.setBigDecimal(4, total);
                s.executeUpdate();
            }

            try (PreparedStatement s = c.prepareStatement(SQL_DECREASE_PRODUCT_QUANTITY)) {
                s.setInt(1, qty);
                s.setLong(2, product);
                s.executeUpdate();
            }

            c.commit();
        }
    }

    public List<Order> findByUser(long user) throws SQLException {
        List<Order> result = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(SQL_FIND_ORDERS_BY_USER)) {
            s.setLong(1, user);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    result.add(map(r));
                }
            }
        }
        return result;
    }

    public void cancel(long order, long user) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);

            Long product = null;
            int qty = 0;

            try (PreparedStatement s = c.prepareStatement(SQL_LOCK_ORDER_FOR_CANCEL)) {
                s.setLong(1, order);
                s.setLong(2, user);
                try (ResultSet r = s.executeQuery()) {
                    if (!r.next()) {
                        throw new IllegalArgumentException("Order not found or already cancelled");
                    }
                    product = r.getLong(1);
                    qty = r.getInt(2);
                }
            }

            try (PreparedStatement s = c.prepareStatement(SQL_CANCEL_ORDER)) {
                s.setLong(1, order);
                s.setLong(2, user);
                s.executeUpdate();
            }

            try (PreparedStatement s = c.prepareStatement(SQL_INCREASE_PRODUCT_QUANTITY)) {
                s.setInt(1, qty);
                s.setLong(2, product);
                s.executeUpdate();
            }

            c.commit();
        }
    }

    private Order map(ResultSet r) throws SQLException {
        return new Order(
                r.getLong(1),
                r.getLong(2),
                r.getLong(3),
                r.getString(4),
                r.getInt(5),
                r.getBigDecimal(6),
                r.getString(7),
                r.getTimestamp(8).toLocalDateTime()
        );
    }
}
