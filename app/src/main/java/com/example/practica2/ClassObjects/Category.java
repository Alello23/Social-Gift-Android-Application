package com.example.practica2.ClassObjects;

public class Category {
    private int id;
    private String name;
    private String description;
    private String photo;
    private int categoryParentId;

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public Category(int id, String name, String description, String photo, int categoryParentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.categoryParentId = categoryParentId;
    }
}

