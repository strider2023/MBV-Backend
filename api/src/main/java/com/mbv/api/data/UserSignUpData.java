package com.mbv.api.data;

import com.mbv.persist.enums.AccountType;
import com.mbv.persist.enums.Gender;
import com.mbv.persist.enums.MaritalStatus;
import com.mbv.persist.enums.ResidentialStatus;
import com.mbv.persist.enums.RoleType;
import com.mbv.persist.enums.WorkStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by arindamnath on 20/01/16.
 */
public class UserSignUpData {

    private long id;

    private String hash;

    private String name;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private AccountType accountType;

    private Gender gender;

    private Date dob;

    private MaritalStatus maritalStatus;

    private WorkStatus workStatus;

    private ResidentialStatus residentialStatus;

    private DeviceData deviceData;

    private String userImage;

    private String referralCode;

    private String fatherName;

    private String gcmId;

    private RoleType roleType;

    private Double rating;

    private Long defaults;

    private List<UserLocationData> userLocationDatas;

    private List<UserKYCData> userKYCDatas;

    private List<UserEducationData> userEducationDatas;

    public UserSignUpData() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }

    public List<UserLocationData> getUserLocationDatas() {
        return userLocationDatas;
    }

    public void setUserLocationDatas(List<UserLocationData> userLocationDatas) {
        this.userLocationDatas = userLocationDatas;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public List<UserKYCData> getUserKYCDatas() {
        return userKYCDatas;
    }

    public void setUserKYCDatas(List<UserKYCData> userKYCDatas) {
        this.userKYCDatas = userKYCDatas;
    }

    public List<UserEducationData> getUserEducationDatas() {
        return userEducationDatas;
    }

    public void setUserEducationDatas(List<UserEducationData> userEducationDatas) {
        this.userEducationDatas = userEducationDatas;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getDefaults() {
        return defaults;
    }

    public void setDefaults(Long defaults) {
        this.defaults = defaults;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
}
