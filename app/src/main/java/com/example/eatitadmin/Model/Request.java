package com.example.eatitadmin.Model;

import java.util.List;

public class Request {
    String Phone, Total, Status, OrderID, TableNo, Tax;
    List<Order> orderList;


    public Request() {
    }

    public Request(String phone, String total, String status, String orderID, String tableNo, String tax, List<Order> orderList) {
        Phone = phone;
        Total = total;
        Status = status;
        OrderID = orderID;
        TableNo = tableNo;
        Tax = tax;
        this.orderList = orderList;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getTableNo() {
        return TableNo;
    }

    public void setTableNo(String tableNo) {
        TableNo = tableNo;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

