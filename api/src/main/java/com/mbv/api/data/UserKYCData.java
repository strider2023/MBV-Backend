package com.mbv.api.data;

import com.mbv.persist.enums.KYCType;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserKYCData {

    private Long id;

    private Long userId;

    private KYCType type;

    private String kycId;

    private String imageUrl;

    private Boolean isVerified;

    public UserKYCData() { }

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
