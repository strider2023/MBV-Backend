package com.mbv.persist.dao;

import com.mbv.persist.entity.LoanInterest;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface LoanInterestDAO extends BaseEntityDAO<Long,LoanInterest>{

    public LoanInterest getInterestByAmountAndPeriod(Long amount, Long period);
}
