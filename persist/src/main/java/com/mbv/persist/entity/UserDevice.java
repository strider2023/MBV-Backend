package com.mbv.persist.entity;

import com.mbv.persist.enums.MobileDeviceType;
import com.mbv.persist.enums.MobileOS;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by arindamnath on 23/02/16.
 */
@Entity
@Table(name="user_device")
public class UserDevice extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="device_id")
    private String deviceId;

    @Column(name="imei_id")
    private String imeiId;

    @Column(name="os_type")
    @Enumerated(EnumType.ORDINAL)
    private MobileOS osType;

    @Column(name="os_version")
    private String osVersion;

    @Column(name="brand")
    private String brand;

    @Column(name="model")
    private String model;

    @Column(name="device_name")
    private String deviceName;

    @Column(name="manufacturer")
    private String manufacturer;

    @Column(name="device_type")
    @Enumerated(EnumType.ORDINAL)
    private MobileDeviceType deviceType;

    @Column(name="operator_name")
    private String operator;

    @Column(name="language")
    private String language;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImeiId() {
        return imeiId;
    }

    public void setImeiId(String imeiId) {
        this.imeiId = imeiId;
    }

    public MobileOS getOsType() {
        return osType;
    }

    public void setOsType(MobileOS osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public MobileDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(MobileDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
