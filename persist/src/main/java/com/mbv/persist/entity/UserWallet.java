package com.mbv.persist.entity;

import com.mbv.persist.enums.WalletProviderType;

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
@Table(name="user_wallet")
public class UserWallet extends BaseEntity<Long> implements Serializable {

    @Column(name="wallet_id")
    private String walletId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="amount")
    private Double amount;

    @Column(name="transaction_hash")
    private String transactionHash;

    @Column(name="provider")
    @Enumerated(EnumType.ORDINAL)
    private WalletProviderType provider;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public WalletProviderType getProvider() {
        return provider;
    }

    public void setProvider(WalletProviderType provider) {
        this.provider = provider;
    }
}
