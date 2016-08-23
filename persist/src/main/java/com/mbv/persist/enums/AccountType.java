package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum AccountType {
    USER, AGENT, ADMIN;
    public static AccountType valueOf(int ordinal) {
        AccountType retVal = null;
        for (AccountType deviceType : AccountType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
