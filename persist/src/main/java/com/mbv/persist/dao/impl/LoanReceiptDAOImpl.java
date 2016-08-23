package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.LoanReceiptDAO;
import com.mbv.persist.entity.LoanReceipt;

/**
 * Created by arindamnath on 25/02/16.
 */
public class LoanReceiptDAOImpl extends BaseEntityDAOImpl<LoanReceipt> implements LoanReceiptDAO {

    public LoanReceiptDAOImpl() {
        super(LoanReceipt.class);
    }
}
