package com.mbv.api.data;

import com.mbv.persist.enums.CurrentLocationType;

import java.util.Date;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserLocationData {

    private Long id;

    private Long userId;

    private String address;

    private String city;

    private String state;

    private String country;

    private Long pincode;

    private CurrentLocationType type;

    private Boolean isVerified;

    public UserLocationData() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public CurrentLocationType getType() {
        return type;
    }

    public void setType(CurrentLocationType type) {
        this.type = type;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
