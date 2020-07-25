package com.example.eatitadmin.Model;

public class Foods {


    String Name,Price,Description,Discount,Image,MenuID,FoodID,menuName,status;

    public Foods() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Foods(String name, String price, String description, String discount, String image, String menuID, String foodID, String menu_Name,String Status) {

        Name = name;
        Price = price;
        Description = description;
        Discount = discount;
        Image = image;
        MenuID = menuID;
        FoodID = foodID;
        menuName=menu_Name;
        status=Status;
    }


    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }
}
