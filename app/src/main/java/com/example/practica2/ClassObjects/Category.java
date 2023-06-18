package com.example.practica2.ClassObjects;

public class Category {
    private int id;
    private String name;
    private String description;
    private String photo;
    private int categoryParentId;

    public Category(int id, String name, String description, String photo, int categoryParentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.categoryParentId = categoryParentId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public int getCategoryParentId() {
        return categoryParentId;
    }
}
