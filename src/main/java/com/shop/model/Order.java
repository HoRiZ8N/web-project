package com.shop.model;
import java.math.BigDecimal; import java.time.LocalDateTime;
public class Order {
    private Long id,userId,productId; private String productName,status; private int quantity;
    private BigDecimal totalPrice; private LocalDateTime createdAt;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
    public BigDecimal getTotalPrice(){return totalPrice;} public void setTotalPrice(BigDecimal v){totalPrice=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
