package com.example.eatitadmin.Model;

import java.util.List;

public class Request {
    String phone, name, total, status, orderID,tableNo,tax;
    List<Order> orderList;

    public Request() {
    }

    public Request(String phone, String name, String total, String status, String orderID, String tableNo, List<Order> orderList,String Tax) {
        this.phone = phone;
        this.name = name;
        this.total = total;
        this.status = status;
        this.orderID = orderID;
        this.tableNo = tableNo;
        this.orderList = orderList;
        tax=Tax;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

