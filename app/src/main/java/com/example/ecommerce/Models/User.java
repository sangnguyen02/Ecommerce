package com.example.ecommerce.Models;

public class User {

    private String username;
    private String userPhone;

    public User() {
    }

    public User(String username, String userPhone) {
        this.username = username;
        this.userPhone = userPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

}
