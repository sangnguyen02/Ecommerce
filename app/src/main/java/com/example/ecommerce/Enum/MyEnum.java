package com.example.ecommerce.Enum;



public class MyEnum {
    public enum DriverStatus{
        DENY,
        PENDING,
        ACTIVE,
        BUSY,
        OFFLINE,
        BANNED,
        DEBT,
    }
    public enum PaymentMethod{
        COD,
        PAYPAL,
        ZALOPAY,
        VNPAY
    }
    public enum OrderStatus {
        PENDING,
        ACCEPT,
        PICKEDUP,
        SUCCEED,
        CANCEL,
    }
    public enum VehicleType {
        MOTORBIKE,
        CAR,
        TRUCK,
        AIRPLANE,
        HELICOPTER,
        UFO
    }

}
