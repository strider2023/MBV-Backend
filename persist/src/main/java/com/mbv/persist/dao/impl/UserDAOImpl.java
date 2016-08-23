package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.entity.User;
import com.mbv.persist.enums.AccountType;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

public class UserDAOImpl extends BaseEntityDAOImpl<User> implements UserDAO {

    public UserDAOImpl() {
        super(User.class);
    }

    @Override
    public User getUserByEmail(String email) {
        Criteria criteria = this.getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (User) criteria.uniqueResult();
    }

    @Override
    public User getUserByEmailAndPhone(String email, String phone) {
        Criteria criteria = this.getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        criteria.add(Restrictions.eq("phone", phone));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (User) criteria.uniqueResult();
    }

    @Override
    public User getUserByEmailAndHash(String email, String hash) {
        Criteria criteria = this.getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        criteria.add(Restrictions.eq("hash", hash));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (User) criteria.uniqueResult();
    }

    @Override
    public List<User> getUserList(AccountType type, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<User> users = criteria.list();
        evictCollection(users);
        return users;
    }
}