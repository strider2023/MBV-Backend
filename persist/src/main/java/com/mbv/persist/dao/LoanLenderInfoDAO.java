package com.mbv.persist.dao;

import com.mbv.persist.entity.LoanLenderInfo;
import com.mbv.persist.enums.Status;

import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface LoanLenderInfoDAO extends BaseEntityDAO<Long,LoanLenderInfo> {

    public List<LoanLenderInfo> getLenderDetailsByUserId(Long userId, Status status);

    public LoanLenderInfo getLenderDetailsByLoanId(Long loanId);
}
