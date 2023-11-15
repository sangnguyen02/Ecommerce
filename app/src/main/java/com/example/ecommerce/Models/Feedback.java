package com.example.ecommerce.Models;

public class Feedback {

    private int id;

    private int rating;
    private String comment;

    public Feedback(int id, int rating, String comment) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
    }

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
