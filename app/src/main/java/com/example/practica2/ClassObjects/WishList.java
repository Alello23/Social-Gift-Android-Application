package com.example.practica2.ClassObjects;
import com.example.practica2.ClassObjects.Gift;

import java.util.List;

public class WishList {
    private int id;
    private String name;
    private String description;
    private int userId;
    private List<Gift> gifts;
    private String creationDate;
    private String endDate;

    public WishList(int id, String name, String description, int userId, List<Gift> gifts, String creationDate, String endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.gifts = gifts;
        this.creationDate = creationDate;
        this.endDate = endDate;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
