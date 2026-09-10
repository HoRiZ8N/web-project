package com.shop.controller;
import com.shop.dao.UserDao; import com.shop.model.User; import jakarta.servlet.*; import jakarta.servlet.http.*; import jakarta.servlet.annotation.*; import java.io.*;
@WebServlet("/login") public class LoginServlet extends HttpServlet{
 private final UserDao dao=new UserDao();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{q.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(q,p);}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{
  try{User u=dao.findByUsername(q.getParameter("username"));if(u!=null&&u.getPassword().equals(q.getParameter("password"))){q.getSession().setAttribute("user",u);p.sendRedirect(q.getContextPath()+"/products");}else{q.setAttribute("error","Неверный логин или пароль");doGet(q,p);}}catch(Exception e){throw new ServletException(e);}
 }
}
