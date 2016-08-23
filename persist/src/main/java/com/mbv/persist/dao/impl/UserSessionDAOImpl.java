package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserSessionDAO;
import com.mbv.persist.entity.UserSession;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserSessionDAOImpl extends BaseEntityDAOImpl<UserSession> implements UserSessionDAO {

    public UserSessionDAOImpl() { super(UserSession.class); }

    @Override
    public UserSession getUserSessionByAuthToken(String token) {
        Criteria criteria = this.getSession().createCriteria(UserSession.class);
        criteria.add(Restrictions.eq("sessionId", token));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (UserSession) criteria.uniqueResult();
    }

    @Override
    public List<UserSession> getActiveUserSessions(Long userId) {
        return null;
    }

    @Override
    public List<UserSession> getActiveUserSessionsByDevice(Long userId, Long deviceId) {
        return null;
    }
}
