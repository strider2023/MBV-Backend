package com.mbv.persist.enums;

/**
 * Created by arindamnath on 24/02/16.
 */
public enum CurrentLocationType {
    CURRENT, HOME;
    public static CurrentLocationType valueOf(int ordinal) {
        CurrentLocationType retVal = null;
        for (CurrentLocationType deviceType : CurrentLocationType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
