package com.example.eatitadmin.Notifications;

public class Sender {
    String to;
    public Data data;

    public Sender() {
    }

    public Sender(String to, Data data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
