package com.mbv.persist.entity;

import com.mbv.persist.enums.TransactionType;

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
@Table(name="user_transaction")
public class UserTransaction extends BaseEntity<Long> implements Serializable {

    @Column(name="transaction_id")
    private String transactionId;

    @Column(name="from_wallet_id")
    private Long fromWalletId;

    @Column(name="to_wallet_id")
    private Long toWalletId;

    @Column(name="from_id")
    private Long fromUserId;

    @Column(name="to_id")
    private Long toUserId;

    @Column(name="from_account_id")
    private Long fromAccountId;

    @Column(name="to_account_id")
    private Long toAccountId;

    @Column(name="amount")
    private Long amount;

    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    private TransactionType type;

    @Column(name="loan_id")
    private Long loanId;

    @Column(name="external_id")
    private String externalId;

    @Column(name="note")
    private String note;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getFromWalletId() {
        return fromWalletId;
    }

    public void setFromWalletId(Long fromWalletId) {
        this.fromWalletId = fromWalletId;
    }

    public Long getToWalletId() {
        return toWalletId;
    }

    public void setToWalletId(Long toWalletId) {
        this.toWalletId = toWalletId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
