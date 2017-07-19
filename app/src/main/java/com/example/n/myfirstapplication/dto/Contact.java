package com.example.n.myfirstapplication.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

/**
 * Created by n on 18/07/2017.
 */

@IgnoreExtraProperties
public class Contact {
    public String name;
    public String email;
    public boolean messagePermission;
    public boolean imagePermission;

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
}
