package com.example.n.myfirstapplication;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by n on 7/07/2017.
 */

@IgnoreExtraProperties
class Message {

    public String time;
    public String message;
    public String userTo; //user which receives
    public String userFrom; //user which sent

    public Message(){}

    public Message(String time, String message, String userTo, String userFrom){
        this.time = time;
        this.message = message;
        this.userTo = userTo;
        this.userFrom = userFrom;
    }
}
