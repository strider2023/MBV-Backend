package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserLocationDAO;
import com.mbv.persist.entity.UserLocation;
import com.mbv.persist.enums.CurrentLocationType;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserLocationDAOImpl extends BaseEntityDAOImpl<UserLocation> implements UserLocationDAO {

    public UserLocationDAOImpl() {
        super(UserLocation.class);
    }

    @Override
    public List<UserLocation> getUserLocationInfoById(Long userId, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(UserLocation.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<UserLocation> userLocations = criteria.list();
        evictCollection(userLocations);
        return userLocations;
    }

    @Override
    public UserLocation getUserLocationByDetails(Long userId, String city, String state,
                                                 String country, Long pincode, CurrentLocationType type) {
        Criteria criteria = this.getSession().createCriteria(UserLocation.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("city", city));
        criteria.add(Restrictions.eq("state", state));
        criteria.add(Restrictions.eq("pincode", pincode));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (UserLocation) criteria.uniqueResult();
    }
}
