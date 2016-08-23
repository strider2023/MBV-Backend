package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum Gender {
    MALE, FEMALE, OTHERS;
    public static Gender valueOf(int ordinal) {
        Gender retVal = null;
        for (Gender deviceType : Gender.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
