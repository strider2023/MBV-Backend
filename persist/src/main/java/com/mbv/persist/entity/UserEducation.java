package com.mbv.persist.entity;

import com.mbv.persist.enums.EducationDegreeType;

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
@Table(name="user_education")
public class UserEducation extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="institution_name")
    private String institutionName;

    @Column(name="degree_type")
    @Enumerated(EnumType.ORDINAL)
    private EducationDegreeType type;

    @Column(name="degree_category")
    private Long degreeCategory;

    @Column(name="description")
    private String description;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="country")
    private String country;

    @Column(name="pincode")
    private Long pincode;

    @Column(name="score")
    private Double score;

    @Column(name="report_image_url")
    private String reportImageUrl;

    @Column(name="is_verified")
    private Boolean isVerified;

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

    public EducationDegreeType getType() {
        return type;
    }

    public void setType(EducationDegreeType type) {
        this.type = type;
    }

    public Long getDegreeCategory() {
        return degreeCategory;
    }

    public void setDegreeCategory(Long degreeCategory) {
        this.degreeCategory = degreeCategory;
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

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getReportImageUrl() {
        return reportImageUrl;
    }

    public void setReportImageUrl(String reportImageUrl) {
        this.reportImageUrl = reportImageUrl;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
