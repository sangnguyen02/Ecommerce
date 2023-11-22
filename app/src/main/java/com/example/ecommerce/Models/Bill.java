package com.example.ecommerce.Models;

import java.util.Date;

public class Bill {
    private int bId;
    private Order order;
    private Date timeStamp;
    private Feedback feedback;

    private float promotion;

    public void setbId(int bId) {
        this.bId = bId;
    }

    public float getPromotion() {
        return promotion;
    }

    public void setPromotion(float promotion) {
        this.promotion = promotion;
    }

    public Bill(int bId, Order order, Date timeStamp, Feedback feedback) {
        this.bId = bId;
        this.order = order;
        this.timeStamp = timeStamp;
        this.feedback = feedback;
        this.promotion = CalculatePromotion(order.getPrice());
    }

    public Bill() {
    }

    public int getbId() {
        return bId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    private float CalculatePromotion(float price){
        return this.order.getPrice() *25 / 100;
    }
}
