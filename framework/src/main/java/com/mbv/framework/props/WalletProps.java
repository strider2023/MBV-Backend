package com.mbv.framework.props;

/**
 * Created by arindamnath on 08/03/16.
 */
public class WalletProps {

    private String bankAccount;

    private String ifscCode;

    private String name;

    public WalletProps() {

    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
