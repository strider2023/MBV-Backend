package com.mbv.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="loan_receipt")
public class LoanReceipt extends BaseEntity<Long> implements Serializable {

    @Column(name="loan_id")
    private Long loanId;

    @Column(name="receipt_location")
    private String receiptUrl;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }
}
