package com.example.n.myfirstapplication;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by n on 7/07/2017.
 */

@IgnoreExtraProperties
class Message {

    public String time;
    public String message;
    public String imageUrl;
    public String userTo; //user which receives
    public String userFrom; //user which sent

    public Message(){}

    public Message(String time, String message, String imageUrl, String userTo, String userFrom){
        this.time = time;
        this.message = message;
        this.imageUrl= imageUrl;
        this.userTo = userTo;
        this.userFrom = userFrom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }
}
