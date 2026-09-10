package com.shop.controller;
import com.shop.model.User;import com.shop.service.OrderService;import jakarta.servlet.*;import jakarta.servlet.http.*;import jakarta.servlet.annotation.*;import java.io.*;
@WebServlet("/orders") public class OrderServlet extends HttpServlet{
 private final OrderService service=new OrderService();
 private User user(HttpServletRequest q){return (User)q.getSession().getAttribute("user");}
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{try{q.setAttribute("orders",service.findByUser(user(q).getId()));q.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(q,p);}catch(Exception e){throw new ServletException(e);}}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{try{User u=user(q);String a=q.getParameter("action");if("create".equals(a))service.create(u.getId(),Long.parseLong(q.getParameter("productId")),Integer.parseInt(q.getParameter("quantity")));if("cancel".equals(a))service.cancel(Long.parseLong(q.getParameter("id")),u.getId());p.sendRedirect(q.getContextPath()+("/orders"));}catch(Exception e){q.setAttribute("error",e.getMessage());doGet(q,p);}}
}
