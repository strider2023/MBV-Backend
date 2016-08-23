package com.mbv.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="loan_lender")
public class LoanLenderInfo extends BaseEntity<Long> implements Serializable {

    @Column(name="loan_id")
    private Long loanId;

    @Column(name="lender_id")
    private Long lenderId;

    @Column(name="device_id")
    private Long deviceId;

    @Column(name="amount")
    private Long amount;

    @Column(name="note")
    private String note;

    @Column(name="offer_code")
    private Long offerCode;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(Long offerCode) {
        this.offerCode = offerCode;
    }
}
