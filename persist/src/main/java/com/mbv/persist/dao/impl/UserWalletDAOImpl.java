package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserWalletDAO;
import com.mbv.persist.entity.UserWallet;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserWalletDAOImpl extends BaseEntityDAOImpl<UserWallet> implements UserWalletDAO {

    public UserWalletDAOImpl() {
        super(UserWallet.class);
    }

    @Override
    public UserWallet getWalletInfoByUserId(Long userId) {
        Criteria criteria = this.getSession().createCriteria(UserWallet.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (UserWallet) criteria.uniqueResult();
    }
}
