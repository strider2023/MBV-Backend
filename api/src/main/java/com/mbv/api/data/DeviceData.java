package com.mbv.api.data;

import com.mbv.persist.enums.MobileDeviceType;
import com.mbv.persist.enums.MobileOS;

/**
 * Created by arindamnath on 23/02/16.
 */
public class DeviceData {

    private Long userId;
    private String deviceId;
    private String imeiId;
    private MobileOS mobileOS;
    private String osVersion;
    private String brand;
    private String model;
    private String deviceName;
    private String manufacturer;
    private String language;
    private MobileDeviceType mobileDeviceType;
    private String operatorName;

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

    public MobileOS getMobileOS() {
        return mobileOS;
    }

    public void setMobileOS(MobileOS mobileOS) {
        this.mobileOS = mobileOS;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public MobileDeviceType getMobileDeviceType() {
        return mobileDeviceType;
    }

    public void setMobileDeviceType(MobileDeviceType mobileDeviceType) {
        this.mobileDeviceType = mobileDeviceType;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
