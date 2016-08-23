package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.LoanLenderInfoDAO;
import com.mbv.persist.entity.LoanLenderInfo;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public class LoanLenderInfoDAOImpl extends BaseEntityDAOImpl<LoanLenderInfo> implements LoanLenderInfoDAO {

    public LoanLenderInfoDAOImpl() {
        super(LoanLenderInfo.class);
    }

    @Override
    public List<LoanLenderInfo> getLenderDetailsByUserId(Long userId, Status status) {
        Criteria criteria = this.getSession().createCriteria(LoanLenderInfo.class);
        criteria.add(Restrictions.eq("lenderId", userId));
        if(status == Status.ALL) {
            criteria.add(Restrictions.in("status", Arrays.asList(Status.PENDING, Status.ACTIVE, Status.COMPLETED)));
        } else {
            criteria.add(Restrictions.eq("status", status));
        }
        List<LoanLenderInfo> loanLenderInfos = criteria.list();
        evictCollection(loanLenderInfos);
        return loanLenderInfos;
    }

    @Override
    public LoanLenderInfo getLenderDetailsByLoanId(Long loanId) {
        Criteria criteria = this.getSession().createCriteria(LoanLenderInfo.class);
        criteria.add(Restrictions.eq("loanId", loanId));
        criteria.add(Restrictions.in("status", Arrays.asList(Status.PENDING, Status.ACTIVE, Status.COMPLETED)));
        return (LoanLenderInfo) criteria.uniqueResult();
    }
}
