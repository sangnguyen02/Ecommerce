package com.example.ecommerce.Models;


import com.example.ecommerce.Enum.MyEnum;

public class Order {
    private int id;
    private String pickupLocation;
    private String desination;
    private int price;
    private String clientName;
    private String clientNo;
    private MyEnum.PaymentMethod paymentMethod;
    private MyEnum.VehicleType vehicleType;
    private MyEnum.OrderStatus orderStatus;
    private DriverInfos driverInfos;

    //OrderNoDriverConstructor
    public Order(int id, String pickupLocation, String desination, int price, String clientName, String clientNo, MyEnum.PaymentMethod paymentMethod, MyEnum.VehicleType vehicleType, MyEnum.OrderStatus orderStatus) {
        this.id = id;
        this.pickupLocation = pickupLocation;
        this.desination = desination;
        this.price = price;
        this.clientName = clientName;
        this.clientNo = clientNo;
        this.paymentMethod = paymentMethod;
        this.vehicleType = vehicleType;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDesination() {
        return desination;
    }

    public void setDesination(String desination) {
        this.desination = desination;
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
}
