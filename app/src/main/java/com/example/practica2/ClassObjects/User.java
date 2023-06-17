package com.example.practica2.ClassObjects;

public class User {
    private int id;
    private String name;
    private String last_name;
    private String email;
    private String image;

    public User(int id, String name, String last_name, String email, String image) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
