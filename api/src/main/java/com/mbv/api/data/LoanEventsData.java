package com.mbv.api.data;

import com.mbv.persist.enums.Status;

import java.util.Date;

/**
 * Created by arindamnath on 04/03/16.
 */
public class LoanEventsData {

    private Long id;

    private Long loanId;

    private String loanRefId;

    private Long amount;

    private Date date;

    private Status status;

    public LoanEventsData() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getLoanRefId() {
        return loanRefId;
    }

    public void setLoanRefId(String loanRefId) {
        this.loanRefId = loanRefId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
