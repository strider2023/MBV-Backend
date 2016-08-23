package com.mbv.api.data;

import com.mbv.persist.enums.RoleType;
import com.mbv.persist.enums.Status;

import java.util.Date;
import java.util.List;

/**
 * Created by arindamnath on 04/03/16.
 */
public class LoanData {

    private Long id;

    private UserData loanBorrowerInfo;

    private UserData loanLenderInfo;

    private RoleType roleType;

    private Status status;

    private String loanId;

    private Long amount;

    private Long totalAmount;

    private Long period;

    private String note;

    private String offerCode;

    private Date requestDate;

    private List<LoanEventsData> loanInstallmentData;

    private LoanInterestData loanInterestData;

    public LoanData() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getLoanBorrowerInfo() {
        return loanBorrowerInfo;
    }

    public void setLoanBorrowerInfo(UserData loanBorrowerInfo) {
        this.loanBorrowerInfo = loanBorrowerInfo;
    }

    public UserData getLoanLenderInfo() {
        return loanLenderInfo;
    }

    public void setLoanLenderInfo(UserData loanLenderInfo) {
        this.loanLenderInfo = loanLenderInfo;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public List<LoanEventsData> getLoanInstallmentData() {
        return loanInstallmentData;
    }

    public void setLoanInstallmentData(List<LoanEventsData> loanInstallmentData) {
        this.loanInstallmentData = loanInstallmentData;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public LoanInterestData getLoanInterestData() {
        return loanInterestData;
    }

    public void setLoanInterestData(LoanInterestData loanInterestData) {
        this.loanInterestData = loanInterestData;
    }
}
