package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.LoanInterestDAO;
import com.mbv.persist.entity.LoanInterest;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by arindamnath on 25/02/16.
 */
public class LoanInterestDAOImpl extends BaseEntityDAOImpl<LoanInterest> implements LoanInterestDAO {

    public LoanInterestDAOImpl() {
        super(LoanInterest.class);
    }

    @Override
    public LoanInterest getInterestByAmountAndPeriod(Long amount, Long period) {
        Criteria criteria = this.getSession().createCriteria(LoanInterest.class);
        criteria.add(Restrictions.eq("loanAmount", amount));
        criteria.add(Restrictions.eq("loanPeriod", period));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (LoanInterest) criteria.uniqueResult();
    }
}
