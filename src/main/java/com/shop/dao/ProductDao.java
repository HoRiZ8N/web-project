package com.shop.dao;
import com.shop.model.Product; import com.shop.util.DatabaseConnection; import java.sql.*; import java.util.*;
public class ProductDao {
 public List<Product> findAll()throws SQLException{
  List<Product> x=new ArrayList<>(); String q="SELECT id,name,description,price,quantity FROM products ORDER BY id";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement(q);ResultSet r=s.executeQuery()){while(r.next()){Product p=new Product();p.setId(r.getLong(1));p.setName(r.getString(2));p.setDescription(r.getString(3));p.setPrice(r.getBigDecimal(4));p.setQuantity(r.getInt(5));x.add(p);}} return x;
 }
 public Product findById(long id)throws SQLException{
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement("SELECT id,name,description,price,quantity FROM products WHERE id=?")){s.setLong(1,id);try(ResultSet r=s.executeQuery()){if(!r.next())return null;Product p=new Product();p.setId(r.getLong(1));p.setName(r.getString(2));p.setDescription(r.getString(3));p.setPrice(r.getBigDecimal(4));p.setQuantity(r.getInt(5));return p;}}
 }
 public void create(String n,String d,java.math.BigDecimal p,int q)throws SQLException{
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement("INSERT INTO products(name,description,price,quantity) VALUES(?,?,?,?)")){s.setString(1,n);s.setString(2,d);s.setBigDecimal(3,p);s.setInt(4,q);s.executeUpdate();}
 }
 public void delete(long id)throws SQLException{
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement("DELETE FROM products WHERE id=?")){s.setLong(1,id);s.executeUpdate();}
 }
}
