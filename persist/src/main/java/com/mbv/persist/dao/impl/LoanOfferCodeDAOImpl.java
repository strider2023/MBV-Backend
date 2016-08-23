package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.LoanOfferCodeDAO;
import com.mbv.persist.entity.LoanOfferCode;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public class LoanOfferCodeDAOImpl extends BaseEntityDAOImpl<LoanOfferCode> implements LoanOfferCodeDAO {

    public LoanOfferCodeDAOImpl() {
        super(LoanOfferCode.class);
    }

    @Override
    public List<LoanOfferCode> getOffercode(Status status, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(LoanOfferCode.class);
        if(status == Status.ALL) {
            criteria.add(Restrictions.in("status", Arrays.asList(Status.ACTIVE, Status.COMPLETED)));
        } else {
            criteria.add(Restrictions.eq("status", status));
        }
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<LoanOfferCode> loanOfferCodes = criteria.list();
        evictCollection(loanOfferCodes);
        return loanOfferCodes;
    }

    @Override
    public LoanOfferCode getOfferCodeByDetails(String offerCode) {
        Criteria criteria = this.getSession().createCriteria(LoanOfferCode.class);
        criteria.add(Restrictions.eq("code", offerCode));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (LoanOfferCode) criteria.uniqueResult();
    }
}
