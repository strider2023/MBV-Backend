package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserDeviceDAO;
import com.mbv.persist.entity.UserDevice;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by arindamnath on 23/02/16.
 */
public class UserDeviceDAOImpl extends BaseEntityDAOImpl<UserDevice> implements UserDeviceDAO {

    public UserDeviceDAOImpl() {
        super(UserDevice.class);
    }

    @Override
    public UserDevice getDeviceById(Long userId, String deviceId) {
        Criteria criteria = this.getSession().createCriteria(UserDevice.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("deviceId", deviceId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (UserDevice) criteria.uniqueResult();
    }

    @Override
    public List<UserDevice> getDeviceByUser(Long userId) {
        Criteria criteria = this.getSession().createCriteria(UserDevice.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        List<UserDevice> userDevices = criteria.list();
        evictCollection(userDevices);
        return userDevices;
    }
}
