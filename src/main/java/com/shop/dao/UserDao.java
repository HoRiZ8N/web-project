package com.shop.dao;
import com.shop.model.User; import com.shop.util.DatabaseConnection; import java.sql.*;
public class UserDao {
 public User findByUsername(String username)throws SQLException{
  String sql="SELECT id,username,password,email,role FROM users WHERE username=?";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement(sql)){s.setString(1,username);try(ResultSet r=s.executeQuery()){if(!r.next())return null;return map(r);}}
 }
 public void create(User u)throws SQLException{
  String sql="INSERT INTO users(username,password,email,role) VALUES(?,?,?,'USER')";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement(sql)){s.setString(1,u.getUsername());s.setString(2,u.getPassword());s.setString(3,u.getEmail());s.executeUpdate();}
 }
 public void updateEmail(long id,String email)throws SQLException{
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement s=c.prepareStatement("UPDATE users SET email=? WHERE id=?")){s.setString(1,email);s.setLong(2,id);s.executeUpdate();}
 }
 private User map(ResultSet r)throws SQLException{User u=new User();u.setId(r.getLong("id"));u.setUsername(r.getString("username"));u.setPassword(r.getString("password"));u.setEmail(r.getString("email"));u.setRole(r.getString("role"));return u;}
}
