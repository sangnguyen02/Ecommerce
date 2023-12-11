package com.example.ecommerce.Models;

import android.location.Location;

import com.example.ecommerce.Enum.MyEnum;

public class DriverAccount {
    private String driverID;    //CCCD
    private String username;    //DriverMail
    private String password;    //DriverID

    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public DriverAccount(String driverID, String username, String password, String salt) {
        this.driverID = driverID;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public DriverAccount() {
    }

    public DriverAccount(String driverID, String username, String password) {
        this.driverID = driverID;
        this.username = username;
        this.password = password;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
