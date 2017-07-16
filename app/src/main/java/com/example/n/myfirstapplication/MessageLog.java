package com.example.n.myfirstapplication;

/**
 * Created by johnn on 7/15/2017.
 */

public class MessageLog {

    private String messageLogId;
    private String userName;
    private String lastMessage;
    private String timeStamp;

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
}
