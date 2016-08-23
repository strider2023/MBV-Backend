package com.mbv.persist.enums;

/**
 * Created by arindamnath on 04/03/16.
 */
public enum TransactionType {
    WALLET, LOAN_SEND, LOAN_RECEIVE, PAYMENT_SEND, PAYMENT_RECEIVE, VALIDITY_SEND, VALIDITY_RECEIVE, ALL;
    public static TransactionType valueOf(int ordinal) {
        TransactionType retVal = null;
        for (TransactionType deviceType : TransactionType.values()) {
            if (deviceType.ordinal() == ordinal) {
                retVal = deviceType;
                break;
            }
        }
        return retVal;
    }
}
