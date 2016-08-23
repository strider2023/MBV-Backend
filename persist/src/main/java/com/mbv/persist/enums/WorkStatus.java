package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum WorkStatus {
    STUDENT, UNEMPLOYED, SALARIED, SELF_EMPLOYED;
    public static WorkStatus valueOf(int ordinal) {
        WorkStatus retVal = null;
        for (WorkStatus deviceType : WorkStatus.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
