package com.inno.webproject.controller;

import com.inno.webproject.dao.ProductDao;
import com.inno.webproject.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductDao dao = new ProductDao();

    private User user(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("products", dao.findAll());
            req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = user(req);
        if (!"ADMIN".equals(u.role())) {
            resp.sendError(403);
            return;
        }
        try {
            String action = req.getParameter("action");
            if ("add".equals(action)) {
                dao.create(
                        req.getParameter("name"),
                        req.getParameter("description"),
                        new BigDecimal(req.getParameter("price")),
                        Integer.parseInt(req.getParameter("quantity"))
                );
            } else if ("delete".equals(action)) {
                dao.delete(Long.parseLong(req.getParameter("id")));
            }
            resp.sendRedirect(req.getContextPath() + "/products");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
