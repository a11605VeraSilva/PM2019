package com.example.tp_android.Models;

public class User {

    private String userid, username, userEmail;

    public User() {
    }

    public User(String userid, String username, String userEmail) {
        this.userid = userid;
        this.username = username;
        this.userEmail = userEmail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
