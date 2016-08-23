package com.mbv.persist.entity;

import com.mbv.persist.enums.CurrentLocationType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="user_location")
public class UserLocation extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="address")
    private String address;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="country")
    private String country;

    @Column(name="pincode")
    private Long pincode;

    @Column(name="months_of_occupation")
    private Date monthsOfOccupation;

    @Column(name="is_verified")
    private Boolean isVerified;

    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    private CurrentLocationType type;

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

    public Date getMonthsOfOccupation() {
        return monthsOfOccupation;
    }

    public void setMonthsOfOccupation(Date monthsOfOccupation) {
        this.monthsOfOccupation = monthsOfOccupation;
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
