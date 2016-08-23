package com.mbv.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 04/03/16.
 */
@Entity
@Table(name="bank_codes")
public class BankCodes extends BaseEntity<Long> implements Serializable {

    @Column(name="ifsc_code")
    private String ifscCode;

    @Column(name="bank_name")
    private String bankName;

    @Column(name="bank_branch")
    private String branch;

    @Column(name="bank_address")
    private String address;

    @Column(name="bank_city")
    private String city;

    @Column(name="bank_district")
    private String district;

    @Column(name="bank_state")
    private String state;

    @Column(name="bank_logo")
    private String logo;

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
