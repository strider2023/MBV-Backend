package com.mbv.api.data;

import com.mbv.persist.enums.OfferCodeType;
import com.mbv.persist.enums.Status;

/**
 * Created by arindamnath on 30/03/16.
 */
public class OfferCodeData {

    private Long id;

    private Long userId;

    private UserData userData;

    private String offerCode;

    private Long usageCounter;

    private Long maxUsageCounter;

    private OfferCodeType type;

    private Status status;

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

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public Long getUsageCounter() {
        return usageCounter;
    }

    public void setUsageCounter(Long usageCounter) {
        this.usageCounter = usageCounter;
    }

    public OfferCodeType getType() {
        return type;
    }

    public void setType(OfferCodeType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getMaxUsageCounter() {
        return maxUsageCounter;
    }

    public void setMaxUsageCounter(Long maxUsageCounter) {
        this.maxUsageCounter = maxUsageCounter;
    }
}
