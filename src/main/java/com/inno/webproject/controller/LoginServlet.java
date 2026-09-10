package com.inno.webproject.controller;

import com.inno.webproject.dao.UserDao;
import com.inno.webproject.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao dao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = dao.findByUsername(req.getParameter("username"));
            if (user != null && user.password().equals(req.getParameter("password"))) {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/products");
            } else {
                req.setAttribute("error", "Invalid username or password");
                doGet(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
