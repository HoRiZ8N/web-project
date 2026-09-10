package com.shop.controller;
import com.shop.dao.UserDao;import com.shop.model.User;import jakarta.servlet.*;import jakarta.servlet.http.*;import jakarta.servlet.annotation.*;import java.io.*;
@WebServlet("/register") public class RegisterServlet extends HttpServlet{
 private final UserDao dao=new UserDao();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{q.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(q,p);}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{try{String n=q.getParameter("username"),pw=q.getParameter("password"),e=q.getParameter("email");if(dao.findByUsername(n)!=null){q.setAttribute("error","Пользователь уже существует");doGet(q,p);return;}dao.create(new User(n,pw,e));p.sendRedirect(q.getContextPath()+"/login");}catch(Exception ex){throw new ServletException(ex);}}
}
