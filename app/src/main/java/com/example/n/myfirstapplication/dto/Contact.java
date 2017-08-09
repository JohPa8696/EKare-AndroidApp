package com.example.n.myfirstapplication.dto;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.SimpleFormatter;

/**
 * Created by n on 18/07/2017.
 */

@IgnoreExtraProperties
public class Contact implements Comparable {
    public String name;
    public String email;
    public boolean messagePermission;
    public boolean imagePermission;
    public String lastMessage ="";
    public String date ="";
    public String deviceToken;


    public Contact(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Contact(String email){
        this.name = "name";
        this.email = email;
        this.messagePermission = true;
        this.imagePermission = true;
    }

    public Contact(String name, String email, boolean messagePermission, boolean imagePermission){
        this.name = name;
        this.email = email;
        this.messagePermission = messagePermission;
        this.imagePermission = imagePermission;
    }
    public Contact(String name, String email, boolean messagePermission, boolean imagePermission,String lastMessage, String timestamp){
        this.name = name;
        this.email = email;
        this.messagePermission = messagePermission;
        this.imagePermission = imagePermission;
        this.lastMessage = lastMessage;
        this.date= timestamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String timestamp) {
        this.date = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMessagePermission() {
        return messagePermission;
    }

    public void setMessagePermission(boolean messagePermission) {
        this.messagePermission = messagePermission;
    }

    public boolean isImagePermission() {
        return imagePermission;
    }

    public void setImagePermission(boolean imagePermission) {
        this.imagePermission = imagePermission;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) {return true; }
        if(!(o instanceof Contact)){
            return false;
        }
        Contact contact = (Contact) o;
        return (email.equals(contact.email));
    }

    @Override
    public int hashCode(){
        return Objects.hash(email);
    }


    @Override
    public int compareTo(@NonNull Object o) {
        Contact otherContact= (Contact) o;
        DateFormat format = new SimpleDateFormat("MMM dd");

        Date otherDate = null;
        Date thisDate = null;
        try {
            otherDate = format.parse(otherContact.getDate());
            thisDate = format.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return otherDate.compareTo(thisDate)  ;
    }
}
