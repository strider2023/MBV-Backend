package com.mbv.persist.enums;

/**
 * Created by arindamnath on 04/03/16.
 */
public enum RoleType {
    BORROW, LEND, ALL;
    public static RoleType valueOf(int ordinal) {
        RoleType retVal = null;
        for (RoleType deviceType : RoleType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
