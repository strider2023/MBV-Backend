package com.mbv.api.data;

import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;

import java.util.Date;

/**
 * Created by arindamnath on 02/04/16.
 */
public class WalletTransactionDetailsData {

    private Long id;

    private String transactionId;

    private Long amount;

    private TransactionType type;

    private Long loanId;

    private String exteralId;

    private String note;

    private Status status;

    private Date createdOn;

    private Date updatedOn;

    private UserData fromUser;

    private WalletAccountData fromAccount;

    private UserData toUser;

    private WalletAccountData toAccount;

    public WalletTransactionDetailsData() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getExteralId() {
        return exteralId;
    }

    public void setExteralId(String exteralId) {
        this.exteralId = exteralId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public UserData getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserData fromUser) {
        this.fromUser = fromUser;
    }

    public WalletAccountData getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(WalletAccountData fromAccount) {
        this.fromAccount = fromAccount;
    }

    public UserData getToUser() {
        return toUser;
    }

    public void setToUser(UserData toUser) {
        this.toUser = toUser;
    }

    public WalletAccountData getToAccount() {
        return toAccount;
    }

    public void setToAccount(WalletAccountData toAccount) {
        this.toAccount = toAccount;
    }
}
