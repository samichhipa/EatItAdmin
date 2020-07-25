package com.example.eatitadmin.Notifications;

public class Data {

    String sent_id,title,body,user_id,notification_type;

    public Data() {
    }

    public Data(String sent_id, String title, String body, String user_id, String notification_type) {
        this.sent_id = sent_id;
        this.title = title;
        this.body = body;
        this.user_id = user_id;
        this.notification_type = notification_type;
    }

    public String getSent_id() {
        return sent_id;
    }

    public void setSent_id(String sent_id) {
        this.sent_id = sent_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }
}
