package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.VerificationOtpDAO;
import com.mbv.persist.entity.VerificationOTP;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by arindamnath on 23/02/16.
 */
public class VerificationOtpDAOImpl extends BaseEntityDAOImpl<VerificationOTP> implements VerificationOtpDAO {

    public VerificationOtpDAOImpl() {
        super(VerificationOTP.class);
    }

    @Override
    public VerificationOTP getDetialsByCode(String otpCode, Long userId) {
        Criteria criteria = this.getSession().createCriteria(VerificationOTP.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("code", otpCode));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (VerificationOTP) criteria.uniqueResult();
    }

    @Override
    public List<VerificationOTP> getActiveOTPRequests(Long userId) {
        Criteria criteria = this.getSession().createCriteria(VerificationOTP.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        List<VerificationOTP> verificationOTPs = criteria.list();
        evictCollection(verificationOTPs);
        return verificationOTPs;
    }
}
