package com.example.ecommerce.Models;

public class User {
    private String userId;
    private String name;
    private int age;
    private String phoneNumber;
    private String vehicleName;
    private String vehicleType;
    private String image;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String name, int age, String phoneNumber, String vehicleName, String vehicleType) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.image = image;
    }





    public String getuserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getImage() {
        return image;
    }
}