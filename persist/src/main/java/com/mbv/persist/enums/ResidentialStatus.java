package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum ResidentialStatus {
    OWNED, RENTAL, PARENTAL, TEMPORARY_SHARING,HOSTEL;
    public static ResidentialStatus valueOf(int ordinal) {
        ResidentialStatus retVal = null;
        for (ResidentialStatus deviceType : ResidentialStatus.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
