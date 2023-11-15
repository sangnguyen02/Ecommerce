package com.example.ecommerce.Models;


import android.location.Location;

import com.example.ecommerce.Enum.MyEnum;

import java.io.Serializable;

public class  Order implements Serializable {
    private int id;
    private Location pickupLocation;
    private Location destination;
    private int price;
    private String clientName;
    private String clientNo;
    private MyEnum.PaymentMethod paymentMethod;
    private MyEnum.VehicleType vehicleType;
    private MyEnum.OrderStatus orderStatus;
    private DriverInfos driverInfos;

    //OrderNoDriverConstructor
    public Order(int id, Location pickupLocation, Location desination, int price, String clientName, String clientNo, MyEnum.PaymentMethod paymentMethod, MyEnum.VehicleType vehicleType, MyEnum.OrderStatus orderStatus) {
        this.id = id;
        this.pickupLocation = pickupLocation;
        this.destination = desination;
        this.price = price;
        this.clientName = clientName;
        this.clientNo = clientNo;
        this.paymentMethod = paymentMethod;
        this.vehicleType = vehicleType;
        this.orderStatus = orderStatus;
    }
    public Order() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }


    public Location getDesination() {
        return destination;
    }

    public void setDesination(Location desination) {
        this.destination = desination;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public MyEnum.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(MyEnum.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public MyEnum.VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(MyEnum.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public MyEnum.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(MyEnum.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public DriverInfos getDriverInfos() {
        return driverInfos;
    }

    public void setDriverInfos(DriverInfos driverInfos) {
        this.driverInfos = driverInfos;
    }

    public void setPickupLocation(Location pickupLocation) {
        if (pickupLocation != null) {
            this.pickupLocation = pickupLocation;
        } else {
            // Handle null input, throw exception, or log a warning.
        }
    }


}
