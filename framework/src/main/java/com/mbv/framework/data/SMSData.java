package com.mbv.framework.data;

/**
 * Created by arindamnath on 24/02/16.
 */
public class SMSData {

    private String to;

    private String senderId;

    private String message;

    public SMSData() { }

    public SMSData(String to, String senderId, String message) {
        this.to = to;
        this.senderId = senderId;
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
