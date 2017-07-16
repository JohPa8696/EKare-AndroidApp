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
    public String timeDate;

    public Message(){}

    public Message( String imageUrl, String message, String sender,String timeDate){
        this.timeDate = timeDate;
        this.message = message;
        this.imageUrl= imageUrl;
        this.sender = sender;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String time) {
        this.timeDate = time;
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
