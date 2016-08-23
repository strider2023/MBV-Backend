package com.mbv.persist.entity;

import com.mbv.persist.enums.OfferCodeType;

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
@Table(name="loan_offer_code")
public class LoanOfferCode extends BaseEntity<Long> implements Serializable {

    @Column(name="code")
    private String code;

    @Column(name="user_id")
    private Long userId;

    @Column(name="usage_count")
    private Long usageCount;

    @Column(name="max_usage_count")
    private Long usageMaxCount;

    @Column(name="offer_type")
    @Enumerated(EnumType.ORDINAL)
    private OfferCodeType type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public OfferCodeType getType() {
        return type;
    }

    public void setType(OfferCodeType type) {
        this.type = type;
    }

    public Long getUsageMaxCount() {
        return usageMaxCount;
    }

    public void setUsageMaxCount(Long usageMaxCount) {
        this.usageMaxCount = usageMaxCount;
    }
}
