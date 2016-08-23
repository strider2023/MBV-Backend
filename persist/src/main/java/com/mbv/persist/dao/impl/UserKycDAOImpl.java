package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserKycDAO;
import com.mbv.persist.entity.UserKYC;
import com.mbv.persist.enums.KYCType;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserKycDAOImpl extends BaseEntityDAOImpl<UserKYC> implements UserKycDAO {

    public UserKycDAOImpl() {
        super(UserKYC.class);
    }

    @Override
    public List<UserKYC> getUserKYCInfoById(Long userId, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(UserKYC.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<UserKYC> userKYCs = criteria.list();
        evictCollection(userKYCs);
        return userKYCs;
    }

    @Override
    public UserKYC getUserKYCByDetials(Long userId, String id, KYCType type) {
        Criteria criteria = this.getSession().createCriteria(UserKYC.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("kycId", id));
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (UserKYC) criteria.uniqueResult();
    }
}
