package com.inno.webproject.controller;

import com.inno.webproject.dao.UserDao;
import com.inno.webproject.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final UserDao dao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            String email = req.getParameter("email");
            dao.updateEmail(user.id(), email);
            User updated = new User(user.id(), user.username(), user.password(), email, user.role());
            req.getSession().setAttribute("user", updated);
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
