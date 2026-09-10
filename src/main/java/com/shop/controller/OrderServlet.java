package com.shop.controller;

import com.shop.model.User;
import com.shop.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final OrderService service = new OrderService();

    private User user(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("orders", service.findByUser(user(req).getId()));
            req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User u = user(req);
            String action = req.getParameter("action");
            if ("create".equals(action)) {
                service.create(
                        u.getId(),
                        Long.parseLong(req.getParameter("productId")),
                        Integer.parseInt(req.getParameter("quantity"))
                );
            }
            if ("cancel".equals(action)) {
                service.cancel(Long.parseLong(req.getParameter("id")), u.getId());
            }
            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
