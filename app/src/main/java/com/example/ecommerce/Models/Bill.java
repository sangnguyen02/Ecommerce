package com.example.ecommerce.Models;

import java.util.Date;

public class Bill {
    private int bId;
    private Order order;
    private Date timeStamp;
    private Feedback feedback;

    public Bill(int bId, Order order, Date timeStamp, Feedback feedback) {
        this.bId = bId;
        this.order = order;
        this.timeStamp = timeStamp;
        this.feedback = feedback;
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
}
