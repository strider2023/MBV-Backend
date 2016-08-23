package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserEducationDAO;
import com.mbv.persist.entity.UserEducation;
import com.mbv.persist.enums.EducationDegreeType;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserEducationDAOImpl extends BaseEntityDAOImpl<UserEducation> implements UserEducationDAO {

    public UserEducationDAOImpl() {
        super(UserEducation.class);
    }

    @Override
    public List<UserEducation> getEducationByUserId(Long userId, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(UserEducation.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<UserEducation> userEducations = criteria.list();
        evictCollection(userEducations);
        return userEducations;
    }

    @Override
    public UserEducation getEducationByDetails(Long userId, String institutionName, EducationDegreeType type, String city, String state) {
        Criteria criteria = this.getSession().createCriteria(UserEducation.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("institutionName", institutionName));
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("city", city));
        criteria.add(Restrictions.eq("state", state));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (UserEducation) criteria.uniqueResult();
    }
}
