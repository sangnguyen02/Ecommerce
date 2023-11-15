package com.example.ecommerce.Models;

import com.example.ecommerce.Enum.MyEnum;

import java.util.List;

public class DriverInfos {
    private String phoneNo;
    private String name;
    private String mail;
    private String id;
    private String license;
    private float avgRating;
    private int balance;
    private String picture;
    private String bankAccount;
    private String bankName;
    private List<Bill> bills;
    private MyEnum.DriverStatus driverStatus;

    //Driver Information Without Bill
    public DriverInfos(String phoneNo, String name, String mail, String id, String license, float avgRating, int balance, String picture, String bankAccount, MyEnum.DriverStatus driverStatus) {
        this.phoneNo = phoneNo;
        this.name = name;
        this.mail = mail;
        this.id = id;
        this.license = license;
        this.avgRating = avgRating;
        this.balance = balance;
        this.picture = picture;
        this.bankAccount = bankAccount;
        this.driverStatus = driverStatus;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public MyEnum.DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setDriverStatus(MyEnum.DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }
}
