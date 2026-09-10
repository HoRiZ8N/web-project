package com.shop.service;

import com.shop.dao.OrderDao;
import com.shop.dao.ProductDao;
import com.shop.model.Order;
import com.shop.model.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private final OrderDao orders = new OrderDao();
    private final ProductDao products = new ProductDao();

    public void create(long user, long product, int qty) throws SQLException {
        Product p = products.findById(product);
        if (p == null) {
            throw new IllegalArgumentException("Товар не найден");
        }
        if (qty <= 0 || qty > p.getQuantity()) {
            throw new IllegalArgumentException("Недостаточное количество товара");
        }
        orders.create(user, product, qty, p.getPrice().multiply(BigDecimal.valueOf(qty)));
    }

    public List<Order> findByUser(long id) throws SQLException {
        return orders.findByUser(id);
    }

    public void cancel(long order, long user) throws SQLException {
        orders.cancel(order, user);
    }
}
