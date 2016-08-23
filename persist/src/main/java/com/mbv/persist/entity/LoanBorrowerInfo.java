package com.mbv.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="loan_master")
public class LoanBorrowerInfo extends BaseEntity<Long> implements Serializable {

    @Column(name="loan_id")
    private String loanId;

    @Column(name="borrower_id")
    private Long borrowerId;

    @Column(name="device_id")
    private Long deviceId;

    @Column(name="amount")
    private Long amount;

    @Column(name="period")
    private Long period;

    @Column(name="interest_id")
    private Long interestId;

    @Column(name="note")
    private String note;

    @Column(name="offer_code")
    private Long offerCode;

    @Column(name="category")
    private Long category;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
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

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
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

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
}
