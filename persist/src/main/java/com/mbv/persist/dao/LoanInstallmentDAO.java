package com.mbv.persist.dao;

import com.mbv.persist.entity.LoanInstallment;

import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface LoanInstallmentDAO extends BaseEntityDAO<Long,LoanInstallment> {

    public List<LoanInstallment> getInstallmentDetailsByLoanId(Long loanId);

    public List<LoanInstallment> getUpcomingInstallmentsByUserId(Long userId, boolean asLender);
}
