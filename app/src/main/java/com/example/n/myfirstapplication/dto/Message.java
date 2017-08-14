package com.example.n.myfirstapplication.dto;

//import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Message Class represent notifications sent from the server.
 * Created by n on 7/07/2017.
 */

public class Message {
    public String imageURL;
    public String message;
    public String sender; //user which send
    public String time;
    public String date;

    public Message(){}

    public Message( String imageURL, String message, String sender,String time, String date) {
        this.imageURL = imageURL;
        this.message = message;
        this.sender = sender;
        this.time = time;
        this.date = date;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


}
