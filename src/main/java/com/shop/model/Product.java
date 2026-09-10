package com.shop.model;
import java.math.BigDecimal;
public class Product {
    private Long id; private String name, description; private BigDecimal price; private int quantity;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
}
