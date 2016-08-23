package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.LoanInstallmentDAO;
import com.mbv.persist.entity.LoanInstallment;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public class LoanInstallmentDAOImpl extends BaseEntityDAOImpl<LoanInstallment> implements LoanInstallmentDAO {

    public LoanInstallmentDAOImpl() {
        super(LoanInstallment.class);
    }

    @Override
    public List<LoanInstallment> getInstallmentDetailsByLoanId(Long loanId) {
        Criteria criteria = this.getSession().createCriteria(LoanInstallment.class);
        criteria.add(Restrictions.eq("loanId", loanId));
        criteria.add(Restrictions.in("status", Arrays.asList(Status.PENDING, Status.ACTIVE, Status.COMPLETED)));
        List<LoanInstallment> loanInstallments = criteria.list();
        evictCollection(loanInstallments);
        return loanInstallments;
    }

    @Override
    public List<LoanInstallment> getUpcomingInstallmentsByUserId(Long userId, boolean asLender) {
        Criteria criteria = this.getSession().createCriteria(LoanInstallment.class);
        if(asLender) {
            criteria.add(Restrictions.eq("lenderId", userId));
        } else {
            criteria.add(Restrictions.eq("borrowerId", userId));
        }
        criteria.add(Restrictions.eq("status", Status.PENDING));
        List<LoanInstallment> lendInstallments = criteria.list();
        evictCollection(lendInstallments);
        return lendInstallments;
    }
}
