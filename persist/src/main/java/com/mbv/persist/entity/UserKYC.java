package com.mbv.persist.entity;

import com.mbv.persist.enums.KYCType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="user_kyc")
public class UserKYC extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    private KYCType type;

    @Column(name="kyc_id")
    private String kycId;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="is_verified")
    private Boolean isVerified;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public KYCType getType() {
        return type;
    }

    public void setType(KYCType type) {
        this.type = type;
    }

    public String getKycId() {
        return kycId;
    }

    public void setKycId(String kycId) {
        this.kycId = kycId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
