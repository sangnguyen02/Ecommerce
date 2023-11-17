package com.example.ecommerce.Models;

import android.location.Location;

import com.example.ecommerce.Enum.MyEnum;

public class DriverAccount {
    public DriverAccount(String phoneNo, String mail, String password, Location currentLocation, MyEnum.DriverStatus driverStatus) {
        this.phoneNo = phoneNo;
        this.mail = mail;
        this.password = password;
        this.currentLocation = currentLocation;
        this.driverStatus = driverStatus;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public MyEnum.DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(MyEnum.DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    private String phoneNo;
    private String mail;
    private String password;
    private Location currentLocation;
    private MyEnum.DriverStatus driverStatus;


}
