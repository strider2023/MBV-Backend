package com.mbv.persist.entity;

import com.mbv.persist.enums.AccountType;
import com.mbv.persist.enums.Currency;
import com.mbv.persist.enums.Gender;
import com.mbv.persist.enums.MaritalStatus;
import com.mbv.persist.enums.ResidentialStatus;
import com.mbv.persist.enums.RoleType;
import com.mbv.persist.enums.WorkStatus;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by arindamnath on 20/01/16.
 */
@Entity
@Table(name="user")
public class User extends BaseEntity<Long> implements Serializable {

    @Column(name="firstname")
    private String firstname;

    @Column(name="middlename")
    private String middlename;

    @Column(name="lastname")
    private String lastname;

    @Column(name="email")
    private String email;

    @Column(name="hash")
    private String hash;

    @Column(name="phone")
    private String phone;

    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    private AccountType type;

    @Column(name="dob")
    private Date dob;

    @Column(name="gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name="marital_status")
    @Enumerated(EnumType.ORDINAL)
    private MaritalStatus maritalStatus;

    @Column(name="work_status")
    @Enumerated(EnumType.ORDINAL)
    private WorkStatus workStatus;

    @Column(name="residential_status")
    @Enumerated(EnumType.ORDINAL)
    private ResidentialStatus residentialStatus;

    @Column(name="currency")
    @Enumerated(EnumType.ORDINAL)
    private Currency currency;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="referral_code")
    private String referralCode;

    @Column(name="role")
    @Enumerated(EnumType.ORDINAL)
    private RoleType role;

    @Column(name="father")
    private String fatherName;

    @Column(name="gcm_id")
    private String gcmId;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public WorkStatus getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }

    public ResidentialStatus getResidentialStatus() {
        return residentialStatus;
    }

    public void setResidentialStatus(ResidentialStatus residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
}
