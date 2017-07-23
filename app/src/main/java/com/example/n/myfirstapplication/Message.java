package com.example.n.myfirstapplication;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by n on 7/07/2017.
 */

@IgnoreExtraProperties
public class Message {
    public String imageUrl;
    public String message;
    public String sender; //user which send
    public String time;
    public String date;

    public Message(){}

    public Message( String imageUrl, String message, String sender,String time, String date) {
        this.time = time;
        this.date = date;
        this.message = message;
        this.imageUrl = imageUrl;
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


}
