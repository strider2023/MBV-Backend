package com.mbv.framework.props;

/**
 * Created by arindamnath on 24/02/16.
 */
public class SMSProps {

    private String username;

    private String password;

    private String defaultSender;

    public SMSProps() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultSender() {
        return defaultSender;
    }

    public void setDefaultSender(String defaultSender) {
        this.defaultSender = defaultSender;
    }
}
