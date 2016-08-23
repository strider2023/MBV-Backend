package com.mbv.framework.data;

/**
 * Created by arindamnath on 24/02/16.
 */
public class EmailData {
    private String to;
    private String sub;
    private String body;

    public EmailData(){}

    public EmailData(String to, String sub, String body) {
        super();
        this.to = to;
        this.sub = sub;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
