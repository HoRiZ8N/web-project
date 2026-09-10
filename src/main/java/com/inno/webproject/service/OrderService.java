package com.inno.webproject.service;

import com.inno.webproject.dao.OrderDao;
import com.inno.webproject.dao.ProductDao;
import com.inno.webproject.model.Order;
import com.inno.webproject.model.Product;
import com.inno.webproject.util.DatabasePoolException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private final OrderDao orders = new OrderDao();
    private final ProductDao products = new ProductDao();

    public void create(long user, long product, int qty) throws SQLException, DatabasePoolException {
        Product p = products.findById(product);
        if (p == null) {
            throw new IllegalArgumentException("Product not found");
        }
        if (qty <= 0 || qty > p.quantity()) {
            throw new IllegalArgumentException("Not enough product quantity available");
        }
        orders.create(user, product, qty, p.price().multiply(BigDecimal.valueOf(qty)));
    }

    public List<Order> findByUser(long id) throws SQLException, DatabasePoolException {
        return orders.findByUser(id);
    }

    public void cancel(long order, long user) throws SQLException, DatabasePoolException {
        orders.cancel(order, user);
    }
}
