package com.example.eatitadmin.Model;

public class Token {
    String TokenID,UserPhone;
    private boolean ServerToken;

    public Token() {
    }

    public Token(String tokenID, String userPhone, boolean ServerToken) {
        TokenID = tokenID;
        UserPhone = userPhone;
        this.ServerToken = ServerToken;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public boolean isServerToken() {
        return ServerToken;
    }

    public void setServerToken(boolean serverToken) {
        ServerToken = serverToken;
    }
}
