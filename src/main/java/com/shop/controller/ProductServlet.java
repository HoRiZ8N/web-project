package com.shop.controller;
import com.shop.dao.ProductDao;import com.shop.model.*;import jakarta.servlet.*;import jakarta.servlet.http.*;import jakarta.servlet.annotation.*;import java.io.*;import java.math.BigDecimal;
@WebServlet("/products") public class ProductServlet extends HttpServlet{
 private final ProductDao dao=new ProductDao();
 private User user(HttpServletRequest q){return (User)q.getSession().getAttribute("user");}
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{try{q.setAttribute("products",dao.findAll());q.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(q,p);}catch(Exception e){throw new ServletException(e);}}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{User u=user(q);if(!"ADMIN".equals(u.getRole())){p.sendError(403);return;}try{String a=q.getParameter("action");if("add".equals(a))dao.create(q.getParameter("name"),q.getParameter("description"),new BigDecimal(q.getParameter("price")),Integer.parseInt(q.getParameter("quantity")));else if("delete".equals(a))dao.delete(Long.parseLong(q.getParameter("id")));p.sendRedirect(q.getContextPath()+"/products");}catch(Exception e){throw new ServletException(e);}}
}
