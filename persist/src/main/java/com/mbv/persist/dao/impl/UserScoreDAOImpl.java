package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserScoreDAO;
import com.mbv.persist.entity.UserScore;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by arindamnath on 18/03/16.
 */
public class UserScoreDAOImpl extends BaseEntityDAOImpl<UserScore> implements UserScoreDAO {


    public UserScoreDAOImpl() {
        super(UserScore.class);
    }

    @Override
    public UserScore getUserScoreInfoById(Long userId) {
        Criteria criteria = this.getSession().createCriteria(UserScore.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (UserScore) criteria.uniqueResult();
    }
}
