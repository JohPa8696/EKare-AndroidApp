package com.example.n.myfirstapplication.dto;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by johnn on 7/15/2017.
 */

public class MessageLog implements Comparable {

    private String messageLogId;
    private String profileUri;
    private String userName;
    private String lastMessage;
    private String timeStamp;

    public MessageLog(){}
    public MessageLog(String messageLogId, String userName, String lastMessage, String timeStamp) {
        this.messageLogId = messageLogId;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timeStamp = timeStamp;
    }

    public String getMessageLogId() {
        return messageLogId;
    }

    public void setMessageLogId(String messageLogId) {
        this.messageLogId = messageLogId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    /**
     * Compare message log object base of the sent date
     * @param o
     * @return
     */
    @Override
    public int compareTo(@NonNull Object o) {
        MessageLog otherLog= (MessageLog) o;
        DateFormat format = new SimpleDateFormat("MMM dd");

        Date otherDate = null;
        Date thisDate = null;
        try {
            otherDate = format.parse(otherLog.getTimeStamp());
            thisDate = format.parse(this.timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return otherDate.compareTo(thisDate)  ;
    }
}
