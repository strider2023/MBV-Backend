package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.BankCodesDAO;
import com.mbv.persist.entity.BankCodes;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 04/03/16.
 */
public class BankCodesDAOImpl extends BaseEntityDAOImpl<BankCodes> implements BankCodesDAO {

    public BankCodesDAOImpl() { super(BankCodes.class); }

    @Override
    public BankCodes getBankCodeByIFSC(String ifsc) {
        Criteria criteria = this.getSession().createCriteria(BankCodes.class);
        criteria.add(Restrictions.eq("ifscCode", ifsc));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (BankCodes) criteria.uniqueResult();
    }

    @Override
    public List<BankCodes> getBankCodesByString(Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(BankCodes.class);
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<BankCodes> bankCodeses = criteria.list();
        evictCollection(bankCodeses);
        return bankCodeses;
    }
}
