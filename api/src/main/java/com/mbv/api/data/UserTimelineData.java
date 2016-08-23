package com.mbv.api.data;

import com.mbv.persist.enums.UserTimelineType;

import java.util.Date;

/**
 * Created by arindamnath on 23/02/16.
 */
public class UserTimelineData {

    private Long userId;

    private UserTimelineType userTimelineType;

    private String description;

    private Long referenceUserId;

    private Long referenceLoanId;

    private Date createdOn;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserTimelineType getUserTimelineType() {
        return userTimelineType;
    }

    public void setUserTimelineType(UserTimelineType userTimelineType) {
        this.userTimelineType = userTimelineType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getReferenceUserId() {
        return referenceUserId;
    }

    public void setReferenceUserId(Long referenceUserId) {
        this.referenceUserId = referenceUserId;
    }

    public Long getReferenceLoanId() {
        return referenceLoanId;
    }

    public void setReferenceLoanId(Long referenceLoanId) {
        this.referenceLoanId = referenceLoanId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
