package com.mbv.persist.enums;

/**
 * Created by arindamnath on 23/02/16.
 */
public enum UserTimelineType {
    PROFILE_UPDATE, DEFAULTED, LOAN_REQUESTED, LOAN_APPROVED, APPROVED_LOAN, RATING_RECEIVED, RATING_GIVEN, WALLET;
    public static UserTimelineType valueOf(int ordinal) {
        UserTimelineType retVal = null;
        for (UserTimelineType deviceType : UserTimelineType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
