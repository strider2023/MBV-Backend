package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum MobileDeviceType {
    PHONE, TABLET;
    public static MobileDeviceType valueOf(int ordinal) {
        MobileDeviceType retVal = null;
        for (MobileDeviceType deviceType : MobileDeviceType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
