package com.shop.service;
import com.shop.dao.*; import com.shop.model.*; import java.sql.*; import java.math.BigDecimal; import java.util.*;
public class OrderService {
 private final OrderDao orders=new OrderDao(); private final ProductDao products=new ProductDao();
 public void create(long user,long product,int qty)throws SQLException{
  Product p=products.findById(product); if(p==null)throw new IllegalArgumentException("Товар не найден");
  if(qty<=0||qty>p.getQuantity())throw new IllegalArgumentException("Недостаточное количество товара");
  orders.create(user,product,qty,p.getPrice().multiply(BigDecimal.valueOf(qty)));
 }
 public List<Order> findByUser(long id)throws SQLException{return orders.findByUser(id);}
 public void cancel(long order,long user)throws SQLException{orders.cancel(order,user);}
}
