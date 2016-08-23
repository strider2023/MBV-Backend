package com.mbv.api.data;

/**
 * Created by arindamnath on 04/03/16.
 */
public class WalletAccountData {

    private Long id;

    private Long userId;

    private Long walletId;

    private String hash;

    private String bankAccount;

    private Long bankRefId;

    private Boolean isPrimary;

    private Boolean isSendVerified;

    private Boolean isReceiveVerified;

    private BankCodesData bankIFSCData;

    public WalletAccountData() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getBankRefId() {
        return bankRefId;
    }

    public void setBankRefId(Long bankRefId) {
        this.bankRefId = bankRefId;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
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

    public BankCodesData getBankIFSCData() {
        return bankIFSCData;
    }

    public void setBankIFSCData(BankCodesData bankIFSCData) {
        this.bankIFSCData = bankIFSCData;
    }
}
