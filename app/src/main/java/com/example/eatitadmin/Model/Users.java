package com.example.eatitadmin.Model;

import androidx.recyclerview.widget.RecyclerView;

public class Users {

    String Password,Phone,IsStaff;


    public Users() {
    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
