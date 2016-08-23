package com.mbv.persist.enums;

/**
 * Created by arindamnath on 24/02/16.
 */
public enum KYCType {
    PAN, ADHAAR, PASSPORT, VOTER_ID, BANK, STUDENT_ID;
    public static KYCType valueOf(int ordinal) {
        KYCType retVal = null;
        for (KYCType deviceType : KYCType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
