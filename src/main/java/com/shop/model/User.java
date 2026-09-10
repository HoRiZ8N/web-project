package com.shop.model;
public class User {
    private Long id; private String username, password, email, role;
    public User() {}
    public User(String username,String password,String email){this.username=username;this.password=password;this.email=email;this.role="USER";}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getRole(){return role;} public void setRole(String v){role=v;}
}
