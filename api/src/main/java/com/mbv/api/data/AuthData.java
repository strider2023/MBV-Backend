package com.mbv.api.data;

/**
 * Created by arindamnath on 25/02/16.
 */
public class AuthData {

    private String email;

    private String password;

    private DeviceData deviceData;

    public AuthData() { }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }
}
