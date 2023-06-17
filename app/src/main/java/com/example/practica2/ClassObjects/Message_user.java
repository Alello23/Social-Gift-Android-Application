package com.example.practica2.ClassObjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message_user {
    private int id;
    private String content;
    private int userIdSend;
    private int userIdReceived;
    private String timeStamp;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public Message_user(int id, String content, int userIdSend, int userIdReceived, String timeStamp) {
        this.id = id;
        this.content = content;
        this.userIdSend = userIdSend;
        this.userIdReceived = userIdReceived;
        this.timeStamp = timeStamp;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserIdSend() {
        return userIdSend;
    }

    public void setUserIdSend(int userIdSend) {
        this.userIdSend = userIdSend;
    }

    public int getUserIdReceived() {
        return userIdReceived;
    }

    public void setUserIdReceived(int userIdReceived) {
        this.userIdReceived = userIdReceived;
    }

    public String getTimeStamp() {
        try {
            return dateFormat.parse(timeStamp).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
