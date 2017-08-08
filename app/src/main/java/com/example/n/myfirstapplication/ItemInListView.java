package com.example.n.myfirstapplication;

import java.util.Objects;

/**
 * Created by n on 19/07/2017.
 */

public class ItemInListView {
    private static final int TYPE_SEPARATOR = 0;
    private static final int TYPE_REQUEST = 1;
    private static final int TYPE_CONTACT = 2;

    public String title;
    public String email;
    public String name;
    public boolean isSeparator = false;
    public boolean isRequest = false;
    public boolean isContact = false;
    public boolean messagePermission;
    public boolean imagePermission;
    public int type;

    public ItemInListView(){

    }

    public ItemInListView(String title){
        this.title = title;
        this.isSeparator = true;
        this.type = 0;
    }

    public ItemInListView(String name, String email, int type){
        this.email = email;
        this.name = name;
        this.type = type;
    }

    public ItemInListView(String name, String email, boolean messagePermission, boolean imagePermission){
        this.email = email;
        this.name = name;
        this.messagePermission = messagePermission;
        this.imagePermission = imagePermission;
        type = 2;
    }

    @Override
    public String toString(){
        return (this.name + " " + this.email);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSeparator() {
        return isSeparator;
    }

    public void setSeparator(boolean separator) {
        isSeparator = separator;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public boolean isContact() {
        return isContact;
    }

    public void setContact(boolean contact) {
        isContact = contact;
    }

    public int getType(){
        return type;
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

    public void setType(int type) {
        this.type = type;
    }
}
