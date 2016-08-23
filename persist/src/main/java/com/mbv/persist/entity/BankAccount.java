package com.mbv.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 04/03/16.
 */
@Entity
@Table(name="bank_account")
public class BankAccount extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="wallet_id")
    private Long walletId;

    @Column(name="account_id")
    private String accountId;

    @Column(name="ifsc_ref_id")
    private Long ifscId;

    @Column(name="transfer_code")
    private String transferCode;

    @Column(name="is_primary")
    private Boolean isPrimary;

    @Column(name="is_send_verified")
    private Boolean isSendVerified;

    @Column(name="is_receive_verified")
    private Boolean isReceiveVerified;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getIfscId() {
        return ifscId;
    }

    public void setIfscId(Long ifscId) {
        this.ifscId = ifscId;
    }

    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    public Boolean getIsSendVerified() {
        return isSendVerified;
    }

    public void setIsSendVerified(Boolean isSendVerified) {
        this.isSendVerified = isSendVerified;
    }

    public Boolean getIsReceiveVerified() {
        return isReceiveVerified;
    }

    public void setIsReceiveVerified(Boolean isReceiveVerified) {
        this.isReceiveVerified = isReceiveVerified;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
