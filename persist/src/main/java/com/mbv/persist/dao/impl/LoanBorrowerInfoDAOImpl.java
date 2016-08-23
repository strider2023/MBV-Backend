package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.LoanBorrowerInfoDAO;
import com.mbv.persist.entity.LoanBorrowerInfo;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public class LoanBorrowerInfoDAOImpl extends BaseEntityDAOImpl<LoanBorrowerInfo> implements LoanBorrowerInfoDAO {

    public LoanBorrowerInfoDAOImpl() {
        super(LoanBorrowerInfo.class);
    }

    @Override
    public List<LoanBorrowerInfo> getAvailableRequestsByUser(Long userId, Status status) {
        Criteria criteria = this.getSession().createCriteria(LoanBorrowerInfo.class);
        criteria.add(Restrictions.eq("borrowerId", userId));
        if(status == Status.ALL) {
            criteria.add(Restrictions.in("status", Arrays.asList(Status.PENDING, Status.ACTIVE, Status.COMPLETED)));
        } else {
            criteria.add(Restrictions.eq("status", status));
        }
        List<LoanBorrowerInfo> loanBorrowerInfos = criteria.list();
        evictCollection(loanBorrowerInfos);
        return loanBorrowerInfos;
    }

    @Override
    public List<LoanBorrowerInfo> getOpenBorrowRequests(Long userId,  Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(LoanBorrowerInfo.class);
        criteria.add(Restrictions.ne("borrowerId", userId));
        criteria.add(Restrictions.eq("status", Status.DRAFT));
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<LoanBorrowerInfo> loanBorrowerInfos = criteria.list();
        evictCollection(loanBorrowerInfos);
        return loanBorrowerInfos;
    }
}
