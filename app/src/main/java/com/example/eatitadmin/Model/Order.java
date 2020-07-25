package com.example.eatitadmin.Model;

public class Order {
    String ProductID,Quantity,Price,ProductName,Discount;

    public Order() {
    }

    public Order(String productID, String quantity, String price, String productName, String discount) {
        ProductID = productID;
        Quantity = quantity;
        Price = price;
        ProductName = productName;
        Discount = discount;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
