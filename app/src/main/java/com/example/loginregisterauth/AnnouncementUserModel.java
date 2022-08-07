package com.example.loginregisterauth;

import java.io.Serializable;

public class AnnouncementUserModel implements Serializable{

    public String topic, sender, message;

    public AnnouncementUserModel() {
    }

    public AnnouncementUserModel(String topic, String sender, String message) {
        this.topic = topic;
        this.sender = sender;
        this.message = message;
    }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public String getSender() { return sender; }

    public void setSender(String sender) { this.sender = sender; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}
