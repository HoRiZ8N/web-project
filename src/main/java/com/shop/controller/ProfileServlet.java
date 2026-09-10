package com.shop.controller;
import com.shop.dao.UserDao;import com.shop.model.User;import jakarta.servlet.*;import jakarta.servlet.http.*;import jakarta.servlet.annotation.*;import java.io.*;
@WebServlet("/profile") public class ProfileServlet extends HttpServlet{
 private final UserDao dao=new UserDao();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{q.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(q,p);}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{try{User u=(User)q.getSession().getAttribute("user");String e=q.getParameter("email");dao.updateEmail(u.getId(),e);u.setEmail(e);p.sendRedirect(q.getContextPath()+"/profile");}catch(Exception e){throw new ServletException(e);}}
}
