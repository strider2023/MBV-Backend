package com.mbv.persist.enums;

/**
 * Created by arindamnath on 04/03/16.
 */
public enum OfferCodeType {
    BORROW, LEND, SIGNUP;
    public static OfferCodeType valueOf(int ordinal) {
        OfferCodeType retVal = null;
        for (OfferCodeType deviceType : OfferCodeType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
