package com.example.n.myfirstapplication.dto;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represent an notifications/message log object.
 * notifications/message logs contain the lastest message and the number of unseen notfications for
 * a contact. They are displayed as items in the notifications tab.
 * Created by johnn on 7/15/2017.
 */

public class MessageLog implements Comparable {
    private String messageLogId;
    private String profileUri;
    private String userName;
    private String lastMessage;
    private String timeStamp;
    private int numNotifications;

    public MessageLog(){}
    public MessageLog(String messageLogId, String userName, String lastMessage, String timeStamp, int numNotifications) {
        this.messageLogId = messageLogId;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timeStamp = timeStamp;
        this.numNotifications = numNotifications;
    }

    public int getNumNotifications() {
        return numNotifications;
    }

    public void setNumNotifications(int numNotifications) {
        this.numNotifications = numNotifications;
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
        // First compare number of notifications
        if (this.numNotifications > otherLog.getNumNotifications()){
            return -1;
        }else if (this.numNotifications < otherLog.getNumNotifications()){
            return 1;
        }else{
            return compareDate(otherLog);
        }
    }

    /**
     * Compares 2 message logs and determines which message log has the latest
     * notification
     * @param otherLog
     * @return
     */
    private int compareDate(MessageLog otherLog){
        DateFormat format = new SimpleDateFormat("MMM dd");

        Date otherDate = null;
        Date thisDate = null;
        try {
            if(otherLog.getTimeStamp().equals("") && this.timeStamp.equals("")) {
                return compareName(otherLog);
            }else if(otherLog.getTimeStamp().equals("")){
                return -1;
            }else if(this.timeStamp.equals("")){
                return 1;
            }else{
                otherDate = format.parse(otherLog.getTimeStamp());
                thisDate = format.parse(this.timeStamp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int res = otherDate.compareTo(thisDate);
        if(res == 0){
            res = compareName(otherLog);
        }
        return res;
    }

    /**
     * To order message log in alphabetically order.
     * @param otherLog
     * @return
     */
    private int compareName(MessageLog otherLog){
        return this.userName.compareTo(otherLog.getUserName());
    }
}
