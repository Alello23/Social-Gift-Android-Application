package com.example.practica2.ClassObjects;

public class Gift {
    private int id;
    private int wishlistId;
    private String productUrl;
    private int priority;
    private boolean booked;

    public Gift(int id, int wishlistId, String productUrl, int priority, boolean booked) {
        this.id = id;
        this.wishlistId = wishlistId;
        this.productUrl = productUrl;
        this.priority = priority;
        this.booked = booked;
    }

    // Getters

    public int getId() {
        return id;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isBooked() {
        return booked;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
