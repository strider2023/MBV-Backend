package com.mbv.api.data;

import com.mbv.persist.enums.EducationDegreeType;

import java.util.Date;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserEducationData {

    private Long id;

    private Long userId;

    private String institutionName;

    private EducationDegreeType degreeType;

    private String degreeCategoryName;

    private String description;

    private Date startDate;

    private Date endDate;

    private String city;

    private String country;

    private String state;

    private Long pincode;

    private Double score;

    private String reportUrl;

    private Boolean isVerified;

    public  UserEducationData() { }

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

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public EducationDegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(EducationDegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public String getDegreeCategoryName() {
        return degreeCategoryName;
    }

    public void setDegreeCategoryName(String degreeCategoryName) {
        this.degreeCategoryName = degreeCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date stopDate) {
        this.endDate = stopDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
