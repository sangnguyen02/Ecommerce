package com.example.ecommerce.Models;


import android.location.Location;

import com.example.ecommerce.Enum.MyEnum;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public class  Order implements Serializable {
    private String id;
    private String pickupLocation_Latitude;
    private String pickupLocation_Longtitude;
    private String destination_Latitude;
    private String destination_Longtidue;
    private float price;
    private String clientName;
    private String clientNo;
    private MyEnum.PaymentMethod paymentMethod;
    private MyEnum.VehicleType vehicleType;
    private MyEnum.OrderStatus orderStatus;
    private DriverInfos driverInfos;
    private String datetime;

    //OrderNoDriverConstructor

    public Order(String id, String pickupLocation_Latitude, String pickupLocation_Longtitude, String destination_Latitude, String destination_Longtidue, float price, String clientName, String clientNo, MyEnum.PaymentMethod paymentMethod, MyEnum.VehicleType vehicleType, MyEnum.OrderStatus orderStatus, DriverInfos driverInfos) {
        this.id = id;
        this.pickupLocation_Latitude = pickupLocation_Latitude;
        this.pickupLocation_Longtitude = pickupLocation_Longtitude;
        this.destination_Latitude = destination_Latitude;
        this.destination_Longtidue = destination_Longtidue;
        this.price = price;
        this.clientName = clientName;
        this.clientNo = clientNo;
        this.paymentMethod = paymentMethod;
        this.vehicleType = vehicleType;
        this.orderStatus = orderStatus;
        this.driverInfos = driverInfos;
    }

    public Order(String id, String pickupLocation_Latitude, String pickupLocation_Longtitude, String destination_Latitude, String destination_Longtidue, float price, String clientName, String clientNo, MyEnum.PaymentMethod paymentMethod, MyEnum.VehicleType vehicleType, MyEnum.OrderStatus orderStatus, DriverInfos driverInfos, String datetime) {
        this.id = id;
        this.pickupLocation_Latitude = pickupLocation_Latitude;
        this.pickupLocation_Longtitude = pickupLocation_Longtitude;
        this.destination_Latitude = destination_Latitude;
        this.destination_Longtidue = destination_Longtidue;
        this.price = price;
        this.clientName = clientName;
        this.clientNo = clientNo;
        this.paymentMethod = paymentMethod;
        this.vehicleType = vehicleType;
        this.orderStatus = orderStatus;
        this.driverInfos = driverInfos;
        this.datetime = datetime;
    }

    public Order() {}

    public void setId(String id) {
        this.id = id;
    }

    public void setPickupLocation_Latitude(String pickupLocation_Latitude) {
        this.pickupLocation_Latitude = pickupLocation_Latitude;
    }

    public void setPickupLocation_Longtitude(String pickupLocation_Longtitude) {
        this.pickupLocation_Longtitude = pickupLocation_Longtitude;
    }

    public void setDestination_Latitude(String destination_Latitude) {
        this.destination_Latitude = destination_Latitude;
    }

    public void setDestination_Longtidue(String destination_Longtidue) {
        this.destination_Longtidue = destination_Longtidue;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public void setPaymentMethod(MyEnum.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setVehicleType(MyEnum.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setOrderStatus(MyEnum.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setDriverInfos(DriverInfos driverInfos) {
        this.driverInfos = driverInfos;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public String getPickupLocation_Latitude() {
        return pickupLocation_Latitude;
    }

    public String getPickupLocation_Longtitude() {
        return pickupLocation_Longtitude;
    }

    public String getDestination_Latitude() {
        return destination_Latitude;
    }

    public String getDestination_Longtidue() {
        return destination_Longtidue;
    }

    public float getPrice() {
        return price;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientNo() {
        return clientNo;
    }

    public MyEnum.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public MyEnum.VehicleType getVehicleType() {
        return vehicleType;
    }

    public MyEnum.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public DriverInfos getDriverInfos() {
        return driverInfos;
    }

    public String getDatetime() {
        return datetime;
    }

}
