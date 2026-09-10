package com.shop.dao;
import com.shop.model.Order; import com.shop.util.DatabaseConnection; import java.sql.*; import java.util.*;
public class OrderDao {
 public void create(long user,long product,int qty,java.math.BigDecimal total)throws SQLException{
  try(Connection c=DatabaseConnection.getConnection()){
   c.setAutoCommit(false);
   try(PreparedStatement s=c.prepareStatement("SELECT quantity FROM products WHERE id=? FOR UPDATE")){s.setLong(1,product);try(ResultSet r=s.executeQuery()){if(!r.next()||r.getInt(1)<qty)throw new IllegalArgumentException("Недостаточное количество товара");}}
   try(PreparedStatement s=c.prepareStatement("INSERT INTO orders(user_id,product_id,quantity,total_price) VALUES(?,?,?,?)")){s.setLong(1,user);s.setLong(2,product);s.setInt(3,qty);s.setBigDecimal(4,total);s.executeUpdate();}
   try(PreparedStatement s=c.prepareStatement("UPDATE products SET quantity=quantity-? WHERE id=?")){s.setInt(1,qty);s.setLong(2,product);s.executeUpdate();}
   c.commit();
  }
 }
 public List<Order> findByUser(long user)throws SQLException{
  List<Order> x=new ArrayList<>(); String q="SELECT o.id,o.user_id,o.product_id,p.name,o.quantity,o.total_price,o.status,o.created_at FROM orders o JOIN products p ON p.id=o.product_id WHERE o.user_id=? ORDER BY o.created_at DESC";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement(q)){s.setLong(1,user);try(ResultSet r=s.executeQuery()){while(r.next()){Order o=new Order();o.setId(r.getLong(1));o.setUserId(r.getLong(2));o.setProductId(r.getLong(3));o.setProductName(r.getString(4));o.setQuantity(r.getInt(5));o.setTotalPrice(r.getBigDecimal(6));o.setStatus(r.getString(7));o.setCreatedAt(r.getTimestamp(8).toLocalDateTime());x.add(o);}}} return x;
 }
 public void cancel(long order,long user)throws SQLException{
  try(Connection c=DatabaseConnection.getConnection()){c.setAutoCommit(false);
   Long product=null; int qty=0;
   try(PreparedStatement s=c.prepareStatement("SELECT product_id,quantity FROM orders WHERE id=? AND user_id=? AND status='CREATED' FOR UPDATE")){s.setLong(1,order);s.setLong(2,user);try(ResultSet r=s.executeQuery()){if(!r.next())throw new IllegalArgumentException("Заказ не найден или уже отменен");product=r.getLong(1);qty=r.getInt(2);}}
   try(PreparedStatement s=c.prepareStatement("UPDATE orders SET status='CANCELLED' WHERE id=? AND user_id=?")){s.setLong(1,order);s.setLong(2,user);s.executeUpdate();}
   try(PreparedStatement s=c.prepareStatement("UPDATE products SET quantity=quantity+? WHERE id=?")){s.setInt(1,qty);s.setLong(2,product);s.executeUpdate();}
   c.commit();
  }
 }
}
