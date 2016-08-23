package com.mbv.persist.enums;

/**
 * Created by arindamnath on 04/03/16.
 */
public enum WalletProviderType {
    MBV;
    public static WalletProviderType valueOf(int ordinal) {
        WalletProviderType retVal = null;
        for (WalletProviderType deviceType : WalletProviderType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
