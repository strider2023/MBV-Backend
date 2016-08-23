package com.mbv.api.data;

import com.mbv.persist.enums.WalletProviderType;

/**
 * Created by arindamnath on 04/03/16.
 */
public class WalletData {

    private Long id;

    private Long userId;

    private String walletId;

    private String hash;

    private Double amount;

    private WalletProviderType type;

    public WalletData() {

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

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public WalletProviderType getType() {
        return type;
    }

    public void setType(WalletProviderType type) {
        this.type = type;
    }
}
