package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum MaritalStatus {
    SINGLE, MARRIED, DIVORCED, WIDOWED, SEPARATED;
    public static MaritalStatus valueOf(int ordinal) {
        MaritalStatus retVal = null;
        for (MaritalStatus deviceType : MaritalStatus.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
