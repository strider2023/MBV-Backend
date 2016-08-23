package com.mbv.persist.enums;

public enum Status {

    DRAFT,

    PENDING,

    REVISION_DRAFT,

    INACTIVE,

    ACTIVE,

    REJECTED,

    REVISED,

    CANCELLED,

    DELIVERING,

    STOPPED,

    PAUSED,

    COMPLETED,

    DELETED,

    ARCHIVED,

    ALL;

    public static Status valueOf(int ordinal) {
        Status retVal = null;
        for (Status status : Status.values()) {
            if (status.ordinal() == ordinal) {
                retVal = status;
                break;
            }
        }
        return retVal;
    }
}
