package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum MobileOS {
    ANDROID, IOS, WINDOWS;
    public static MobileOS valueOf(int ordinal) {
        MobileOS retVal = null;
        for (MobileOS deviceType : MobileOS.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
