package com.mbv.persist.entity;

import com.mbv.persist.enums.UserTimelineType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by arindamnath on 23/02/16.
 */
@Entity
@Table(name="user_timeline")
public class UserTimeline extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    private UserTimelineType type;

    @Column(name="description")
    private String description;

    @Column(name="reference_user_id")
    private Long refUserId;

    @Column(name="reference_loan_id")
    private Long refLoanId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserTimelineType getType() {
        return type;
    }

    public void setType(UserTimelineType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Long refUserId) {
        this.refUserId = refUserId;
    }

    public Long getRefLoanId() {
        return refLoanId;
    }

    public void setRefLoanId(Long refLoanId) {
        this.refLoanId = refLoanId;
    }
}
